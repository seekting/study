
package com.seekting.study;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FormatCityActivity extends BaseActivity implements OnClickListener {

    public FormatCityActivity() {
        name = "城市转换";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.format_city_layout);
        Button hot = (Button) findViewById(R.id.format1);
        Button all = (Button) findViewById(R.id.format2);
        hot.setOnClickListener(this);
        all.setOnClickListener(this);

    }

    private JSONObject toJson(List<HotCity> cities) {
        // 先归类
        LinkedHashMap<String, List<HotCity>> map = new LinkedHashMap<String, List<HotCity>>();
//        for (char i = 'a'; i <= 'z'; i++) {
//            map.put(String.valueOf(i), new ArrayList<FormatCityActivity.HotCity>());
//        }
//        for (int i = 0; i < cities.size(); i++) {
//            HotCity city = cities.get(i);
//            System.out.println(city.head);
//            map.get(city.head).add(city);
//        }
        JSONObject cityGroup = new JSONObject();

        JSONArray data = new JSONArray();
        try {
            cityGroup.put("data", data);
            JSONObject obj = new JSONObject();
            obj.put("index", "热门");
            data.put(obj);
            JSONArray jsonArray = new JSONArray();
            obj.put("cs", jsonArray);

            for (int i = 0; i < cities.size(); i++)
            {
//                String key = it.next();
//                List<HotCity> value = map.get(key);
//                for (int i = 0; i < value.size(); i++) {
                    jsonArray.put(cities.get(i).ToString());
//                }
              
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // System.out.println(cityGroup);
        return cityGroup;

    }

    private JSONObject toJson2(List<HotCity> cities) {
        // 先归类
        LinkedHashMap<String, List<HotCity>> map = new LinkedHashMap<String, List<HotCity>>();
        for (char i = 'a'; i <= 'z'; i++) {
            map.put(String.valueOf(i), new ArrayList<FormatCityActivity.HotCity>());
        }
        for (int i = 0; i < cities.size(); i++) {
            HotCity city = cities.get(i);
            System.out.println(city.head);
            map.get(city.head).add(city);
        }
        JSONObject cityGroup = new JSONObject();

        JSONArray data = new JSONArray();
        try {
            cityGroup.put("data", data);

            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();)
            {
                String key = it.next();
                JSONObject obj = new JSONObject();
                obj.put("index", key);
                JSONArray jsonArray = new JSONArray();
                obj.put("cs", jsonArray);
                List<HotCity> value = map.get(key);
                for (int i = 0; i < value.size(); i++) {
                    jsonArray.put(value.get(i).ToString());
                }
                data.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // System.out.println(cityGroup);
        return cityGroup;

    }

    private HotCity parseHotCity(String cityStr) {
        HotCity city = null;
        if (cityStr != null) {
            city = new HotCity();
            int first = cityStr.indexOf("@");
            int second = cityStr.indexOf(">");
            String sub1 = cityStr.substring(first);
            city.name = cityStr.substring(second + 1, first);
            String nameQPJP = sub1.substring(1, sub1.indexOf("#"));
            String[] list = nameQPJP.split("@");

            city.qp = list[1];
            city.jp = list[2];
            city.head = String.valueOf(city.jp.charAt(0));
            String local = "\",\"locale\":\"";
            String cc = ",\"cc\":\"";
            int begin = sub1.indexOf(cc);
            int end = sub1.indexOf(local);
            city.code = sub1.substring(begin + cc.length(), end);
            // System.out.println(city.ToString());
        }
        return city;
    }

    private void doTranslateHot() {
        File sdDir = Environment.getExternalStorageDirectory();
        File citys = new File(sdDir.getAbsolutePath() + "/citys.txt");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<HotCity> cities = new ArrayList<HotCity>();
        try {
            fileReader = new FileReader(citys);
            bufferedReader = new BufferedReader(fileReader);
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                if (line != null) {
                    HotCity city = parseHotCity(line);
                    cities.add(city);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = toJson(cities);
        outputCitys(sdDir, jsonObject, "/hot_citys_json.txt");
    }

    private void outputCitys(File sdDir, JSONObject jsonObject, String fileName) {
        File file = new File(sdDir.getAbsolutePath() + fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void doTranslateAll() {
        File sdDir = Environment.getExternalStorageDirectory();
        File citys = new File(sdDir.getAbsolutePath() + "/citypy.php");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<HotCity> cities = new ArrayList<HotCity>();
        List<Province> provinces = new ArrayList<FormatCityActivity.Province>();
        try {
            fileReader = new FileReader(citys);
            bufferedReader = new BufferedReader(fileReader);
            String line = "";
            Province province = new Province();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line != null) {
                    if (line.contains("<?php") || line.contains("$config['citypy'] = array (")
                            || line.contains("?>") || line.contains(");")) {
                        continue;
                    } else {
                        if (line.contains("),),")) {
                            // 省结尾
                            province = new Province();
                        } else if (!line.contains("'p'")) {
                            // 省开头
                            int end = line.indexOf("=>");
                            province.nqj.setNQJ(line.substring(0, end));
                            provinces.add(province);
                        } else {
                            // 省里面的市区
                            AllCity allCity = new AllCity();
                            int end = line.indexOf("=>");
                            allCity.nqj.setNQJ(line.substring(line.indexOf("'") - 1, end));
                            parsePCXCC(allCity, line);
                            province.cities.add(allCity);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        JSONObject provincesJson = translateAllCity(provinces);
        outputCitys(sdDir, provincesJson, "/provinces_json.txt");

    }

    public JSONObject translateAllCity(List<Province> provinces) {
        JSONObject ps = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            ps.put("ps", array);

            for (int i = 0; i < provinces.size(); i++) {
                JSONObject provinceJson = new JSONObject();
                Province province = provinces.get(i);
                provinceJson.put("pv", province.nqj.toString());
                JSONArray jsonArray = new JSONArray();
                provinceJson.put("cs", jsonArray);
                for (int j = 0; j < province.cities.size(); j++) {
                    AllCity allCity = province.cities.get(j);
                    jsonArray.put(allCity.toJsonObject());

                }
                array.put(provinceJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ps;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.format1:
                doTranslateHot();
                break;
            case R.id.format2:
                doTranslateAll();
                break;
            default:
                break;
        }

    }

    /**
     * 
     */
    private static class HotCity {
        private String head;
        private String name;
        /**
         * 全拼
         */
        private String qp;
        /**
         * 简拼
         */
        private String jp;

        /**
         * 城市码
         */
        private String code;

        @Override
        public String toString() {
            return "City [name=" + name + ", qp=" + qp + ", jp=" + jp + ", code=" + code + "]";
        }

        public String ToString() {
            return name + "@" + qp + "@" + jp + "@" + code;
        }
    }

    private static class Province {
        private NQJ nqj = new NQJ();
        private List<AllCity> cities = new ArrayList<FormatCityActivity.AllCity>();

        @Override
        public String toString() {
            return "省:" + nqj.toString() + "{" + cities.size() + "}";
        }

        public int getSize() {
            return cities.size();
        }
    }

    private static class AllCity {
        private NQJ nqj = new NQJ();
        private NQJ p = new NQJ();
        private NQJ c = new NQJ();
        private NQJ x = new NQJ();
        private String cc;

        private JSONObject toJsonObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cv", nqj.toString());
                jsonObject.put("c", c.toString());
                jsonObject.put("x", x.toString());
                jsonObject.put("cc", cc);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return jsonObject;

        }
    }

    /**
     * '天津#tianjin#tj'
     */
    private static class NQJ {
        private String name;
        private String qp;
        private String jp;

        /**
         * '天津#tianjin#tj'
         * 
         * @param nqjStr
         */
        public void setNQJ(String nqjStr) {
            if (nqjStr != null) {
                
                String str = nqjStr.replaceAll("'", "");
                str=str.trim();
                String[] strs = str.split("#");
                name = strs[0];
                qp = strs[1];
                jp = strs[2];

            }
        }

        @Override
        public String toString() {
            return name + "@" + qp + "@" + jp;
        }

    }

    public static final void parsePCXCC(AllCity city, String str) {
        String p = "'p' => ";
        String c = ",'c' => ";
        String x = ",'x' => ";
        String cc = ",'cc' => '";
        String ccE = "',),";
        int pIndex = str.indexOf(p);
        int cIndex = str.indexOf(c);
        int xIndex = str.indexOf(x);
        int ccIndex = str.indexOf(cc);

        int ccEnd = str.indexOf(ccE);
        if (ccEnd == -1) {
            ccEnd = str.length() - 2;

        }
        city.p.setNQJ(str.substring(pIndex + p.length(), cIndex));
        city.c.setNQJ(str.substring(cIndex + c.length(), xIndex));
        city.x.setNQJ(str.substring(xIndex + x.length(), ccIndex));
        city.cc = (str.substring(ccIndex + cc.length(), ccEnd));

    }
}
