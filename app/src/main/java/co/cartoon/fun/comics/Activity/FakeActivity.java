package co.cartoon.fun.comics.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import co.cartoon.fun.comics.Data.Main_Data;
import co.cartoon.fun.comics.R;

import static android.os.Build.VERSION.SDK_INT;

public class FakeActivity extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList<Main_Data> list;
    private MainAdapter main_adapter;
    private int current_position = 0;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init_ui();
        list = new ArrayList<Main_Data>();
        list.clear();
        displaylist();
    }

    private void init_ui() {
        listview_main = (GridView) findViewById(R.id.listview_main);
    }

    private Main_ParseAsync main_parseAsync = null;
    private GridView listview_main;
    public void displaylist() {
        main_parseAsync = new Main_ParseAsync();
        main_parseAsync.execute();
        if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            listview_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        listview_main.setOnItemClickListener(this);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Main_Data main_data = (Main_Data) main_adapter.getItem(position);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(main_data.portal));
        startActivity(i);
    }


    public class Main_ParseAsync extends AsyncTask<String, Integer, String> {
        String Response;
        Main_Data main_data;
        ArrayList<Main_Data> menuItems = new ArrayList<Main_Data>();
        String i;
        int _id;
        String id;
        String title;
        String portal;
        String thumb;
        String sprit_title[];

        public Main_ParseAsync() {
        }

        @Override
        protected String doInBackground(String... params) {
            String sTag;
            try {
                String str = "http://cion49235.cafe24.com/xml_musicfree/detail/view" + i + ".php?view=" + "1514";
//				Log.i("dsu", "URL : " + str);
                HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                HttpURLConnection.setFollowRedirects(false);
                localHttpURLConnection.setConnectTimeout(15000);
                localHttpURLConnection.setReadTimeout(15000);
                localHttpURLConnection.setRequestMethod("GET");
                localHttpURLConnection.connect();
                InputStream inputStream = new URL(str).openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, "EUC-KR");
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    } else if (eventType == XmlPullParser.END_DOCUMENT) {
                    } else if (eventType == XmlPullParser.START_TAG) {
                        sTag = xpp.getName();
                        if (sTag.equals("Content")) {
                            main_data = new Main_Data();
                            _id = Integer.parseInt(xpp.getAttributeValue(null, "id") + "");
                        } else if (sTag.equals("videoid")) {
                            Response = xpp.nextText() + "";
                        } else if (sTag.equals("subject")) {
                            title = xpp.nextText() + "";
//                            Log.i("dsu", "title : " + title );
                            sprit_title = title.split("-");
                        } else if (sTag.equals("portal")) {
                            portal = xpp.nextText() + "";
                        } else if (sTag.equals("thumb")) {
                            thumb = xpp.nextText() + "";
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        sTag = xpp.getName();
                        if (sTag.equals("Content")) {
                            main_data._id = _id;
                            main_data.id = Response;
                            main_data.title = title;
                            main_data.portal = portal;
                            main_data.category = getString(R.string.app_name);
                            main_data.thumb = thumb;
                            list.add(main_data);
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                    }
                    eventType = xpp.next();
                }
            } catch (SocketTimeoutException localSocketTimeoutException) {
            } catch (ClientProtocolException localClientProtocolException) {
            } catch (IOException localIOException) {
            } catch (Resources.NotFoundException localNotFoundException) {
            } catch (NullPointerException NullPointerException) {
            } catch (Exception e) {
            }
            return Response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            i = "6";
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);
			Log.i("dsu", "Response : " + Response);
            try {
                if (Response != null) {
                    for (int i = 0; ; i++) {
                        if (i >= list.size()) {
//							while (i > list.size()-1){
                            main_adapter = new MainAdapter(context, menuItems, listview_main);
                            listview_main.setAdapter(main_adapter);
                            listview_main.setFocusable(true);
                            listview_main.setSelected(true);
                            listview_main.setSelection(current_position);
                            return;
                        }
                        menuItems.add(list.get(i));
                    }
                }
            } catch (NullPointerException e) {
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class MainAdapter extends BaseAdapter {
        public Context context;
        public int _id = -1;
        public String id = "empty";
        public Cursor cursor;
        public ImageButton bt_favorite;
        public ArrayList<Main_Data> list;
        public GridView listview_main;

        public MainAdapter(Context context, ArrayList<Main_Data> list, GridView listview_main) {
            this.context = context;
            this.list = list;
            this.listview_main = listview_main;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                if (view == null) {
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    view = layoutInflater.inflate(R.layout.main_activity_listrow, parent, false);
                    ViewHolder holder = new ViewHolder();
                    holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
                    holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
                    view.setTag(holder);
                }
                final ViewHolder holder = (ViewHolder) view.getTag();
                BitmapFactory.Options dimensions = new BitmapFactory.Options();
                dimensions.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty, dimensions);
                int height = dimensions.outHeight;
                int width =  dimensions.outWidth;

                Picasso.with(context)
                        .load(list.get(position).thumb)
                        .placeholder(R.drawable.empty)
                        .error(R.drawable.empty)
                        .into(holder.img_imageurl);
                holder.txt_title.setText(list.get(position).title);

            } catch (Exception e) {
            }
            return view;
        }
    }

    private class ViewHolder {
        public ImageView img_imageurl;
        public TextView txt_title;
        public Button bt_favorite;
    }
}
