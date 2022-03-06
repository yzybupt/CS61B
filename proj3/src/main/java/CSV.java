import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSV {

    public static void main(String[] args) {

        String csvFile = "C:\\\\Users\\\\Yin Ziyuan\\\\Desktop\\\\all_comment_data_alldata.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        HashSet<Integer> stock_id = new HashSet<>();
        ConcurrentHashMap<Integer, String> id2time = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, HashSet<String>> idandtime = new ConcurrentHashMap<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int i = 0;
            while ((line = br.readLine()) != null) {
                    // use comma as separator
                i++;
                String[] country = line.split(cvsSplitBy);
                System.out.println("[id= " + country[0] + " , time=" + country[4] + "]");
                if(!country[0].equals("stock_id") & !isContainChinese(country[4])) {
                    stock_id.add(Integer.valueOf(country[0]));
                    HashSet<String> temp = new HashSet<>();
                    temp.add(country[4]);
                    idandtime.put(Integer.valueOf(country[0]), temp);
                }
            }
            for (Integer ids : stock_id) {
                id2time.put(ids, "");
            }

            for (Integer id : id2time.keySet()) {
                HashSet<String> times = idandtime.get(id);
                String temp = "2028-7-1";
                for (String time : times) {
                    if (compare(time,temp)) {
                        temp = time;
                    }
                }
                id2time.put(id, temp);
            }

            File writename = new File("C:\\\\Users\\\\Yin Ziyuan\\\\Desktop\\\\output.csv"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            for (Integer id : id2time.keySet()) {
                out.write(String.valueOf(id));
                out.write(",");
                out.write(id2time.get(id)); // \r\n即为换行
                out.write("\r\n");
            }
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件








        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
            }
        }

    }


    private static boolean compare(String a, String b) {
        String beginTime=new String(a);
        String endTime=new String(b);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date sd1=df.parse(beginTime);
            Date sd2=df.parse(endTime);
            return sd1.before(sd2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
