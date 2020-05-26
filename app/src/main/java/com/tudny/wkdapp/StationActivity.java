package com.tudny.wkdapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.tudny.wkdapp.core.Station;
import com.tudny.wkdapp.ui.stations.StationsFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StationActivity extends AppCompatActivity {

	public static final String DEBUG_TAG = StationActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station);

		Integer stationId = getIntent().getExtras().getInt(StationsFragment.STATION_ID_TO_PASS);
		Station station = Station.choseByNumber(stationId);

		try {
			if(getSupportActionBar() == null) throw new NullPointerException();
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		} catch (Exception e){
			Log.e(DEBUG_TAG, e.getMessage(), e);
		}

		final Button navigateButton = findViewById(R.id.navigate_button);
		navigateButton.setOnClickListener(v -> MainActivity.openNavigation(this, station.getLatitude(),station.getLongitude()));
		
		/*TextView textView = findViewById(R.id.station_name);
		textView.setText(station.getStationName());*/

		//ImageDownload imageDownload = new ImageDownload(this, imageView);
		//imageDownload.execute(url);

		// Picasso.get().setIndicatorsEnabled(true);

		String url = station.getImageURL();
		ImageView imageView = findViewById(R.id.image);
		Picasso.get()
				.load(url)
				.placeholder(R.drawable.ic_file_download_black_24dp)
				.error(R.drawable.ic_error_outline_black_24dp)
				.into(imageView);

		String newTitle = station.getStationName();
		setTitle(newTitle);

		// Download the content from wkd.com.pl

		DownloadStationsContent downloadStationsContent = new DownloadStationsContent();
		try {
			downloadStationsContent.execute(station.getPageURL());
		} catch (Exception e){
			Log.e(DEBUG_TAG, e.getMessage(), e);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateContent(String content){
		TextView stationDescription = findViewById(R.id.station_description);
		stationDescription.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));

		ProgressBar progressBar = findViewById(R.id.progress_bar_content);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("StaticFieldLeak")
	private class DownloadStationsContent extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String content = "";
			String url = params[0];

			try {
				Document doc = Jsoup.connect(url).get();
				Elements tables = doc.getElementsByClass("articleBootstrap item-page  col-xs-12 pull-right normal radius shadow nopadding");
				Elements ps = tables.get(0).getElementsByTag("p");

				// Log.d(DEBUG_TAG, "doInBackground: " + ps.html());

				for(Element element : ps){
					String cont = (element.html() + "\n");
					content = content + cont;
					publishProgress(ps.indexOf(element) * 100 / ps.size());
				}

				publishProgress(100);

				int index = content.indexOf("NFORMACJA");
				//Log.d(DEBUG_TAG, "index: " + index);
				if(index == -1) index = content.length();
				content = content.substring(0, index)
						.replace("<br>", "<br/>")
						.replaceAll("<img[^>]*>", "")
						.replaceAll("<a[^>]*>", "");

				StringBuilder sb = new StringBuilder(content);
				String temp = sb.reverse().toString();
				temp = temp.replaceFirst("I", "");
				sb = new StringBuilder(temp);
				content = sb.reverse().toString();

			} catch (Exception e){
				content = getString(R.string.error_content, e.getMessage());
			}

			return content;
		}

		@Override
		protected void onPostExecute(String content) {
			updateContent(content);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			ProgressBar progressBar = findViewById(R.id.progress_bar_content);
			progressBar.setProgress(values[0]);
		}
	}
}
