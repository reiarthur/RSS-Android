package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome
    private ListView conteudoRSS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use ListView ao invés de TextView - deixe o ID no layout XML com o mesmo nome conteudoRSS
        //isso vai exigir o processamento do XML baixado da internet usando o ParserRSS

        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new CarregaRSStask().execute(RSS_FEED);
    }

    private class CarregaRSStask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String conteudo = "provavelmente deu erro...";
            try {
                conteudo = getRssFeed(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return conteudo;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            //ajuste para usar uma ListView
            //o layout XML a ser utilizado esta em res/layout/itemlista.xml

            try {

                // processando RSS pelo ParserRSS
                List<ItemRSS> parsedRSS = ParserRSS.parse(s);

                //Criando instancia do adapter customizado para exibir titulo e data
                CustomAdapter itemRSSAdapter = new CustomAdapter(MainActivity.this, R.layout.itemlista, parsedRSS);

                conteudoRSS.setAdapter(itemRSSAdapter);
                conteudoRSS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ItemRSS item = (ItemRSS) parent.getItemAtPosition(position);

                        // pegando o url do item clicado
                        String url = item.getLink();

                        // chamando nova intent e passando como extra o url pego anteriormente
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.putExtra("url", url);

                        // iniciando nova activity com um chooser entre browsers
                        // já instalados no celular e a webview criada no projeto
                        startActivity(Intent.createChooser(i, "Escolher browser"));
                    }
                });

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
