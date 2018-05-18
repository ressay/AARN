package revo;





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class QueryUtils {
    public static final String URL_LOGIN = "http://192.168.8.112/medos/php/patient/loginp.php";
    public static final String URL_VERIFY_EXISTS = "http://192.168.8.112/medos/php/patient/exist.php";
    public static final String URL_TQueue = "http://192.168.8.105/medos/php/queue/tqueue.php";


    public static String makeHttpPostRequest (String urlString, Map<String,String> parameters)
    {   String response = null;
        InputStream inputStream=null;
        
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(urlString);
            
            urlConnection = (HttpURLConnection) url.openConnection();
           
            urlConnection.setDoOutput(true);
            
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            String postParameters = buildParametersString(parameters);
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();
            urlConnection.connect();
             
            if (urlConnection.getResponseCode()<=500)
            {
               
                inputStream = urlConnection.getInputStream();
                response=readFromStream(inputStream);
            }
            else
            {
                System.err.println("fqsfqsf");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream!=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
              
            }
            if (urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            return response;
        }
    }

    public static String buildParametersString(Map<String,String> parameters)
    {
        Set<Map.Entry<String,String>> entrySet = parameters.entrySet();
        Iterator<Map.Entry<String,String>> iterator = entrySet.iterator();
        StringBuilder builder = new StringBuilder("");
        boolean isFirstParameter = true;
        while (iterator.hasNext())
        {
            Map.Entry<String,String> entry = iterator.next();
            if (!isFirstParameter)
            {
                builder.append("&");
            }
            builder.append(entry.getKey()+"="+entry.getValue());
            isFirstParameter=false;
        }
        return builder.toString();
    }

    public static String readFromStream (InputStream inputStream)
    {
        StringBuilder builder = new StringBuilder("");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            String line =reader.readLine();
            while (line != null)
            {
                builder.append(line);
                line=reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return builder.toString();
        }
    }

    public static String gethttp(String s)
    {
        return s;
    }
}
