
package com.mb.android.nzbAirPremium.imdb;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;

public class IMDBConverter implements HttpResponseConverter<Imdb> {

	private static String TAG = IMDBConverter.class.toString();
	private final HttpResponseConverter<String> strat;

	public IMDBConverter(HttpResponseConverter<String> strat) {
		this.strat = strat;
	}

	@Override
	public Imdb convert(RestResponse rr) throws Exception {

		final String json = rr.getBodyAsString();

		try {
			final Imdb movie = new Imdb();
			final JSONObject obj = new JSONObject(json);

			movie.setTitle(obj.optString("Title", ""));
			movie.setYear(obj.optString("Year", ""));
			movie.setRated(obj.optString("Rated", ""));
			movie.setReleased(obj.optString("Released", ""));
			movie.setGenre(obj.optString("Genre", ""));
			movie.setDirector(obj.optString("Director", ""));
			movie.setWriter(obj.optString("Writer", ""));
			movie.setActors(obj.optString("Actors", ""));
			movie.setPlot(obj.optString("Plot", ""));
			movie.setImage(obj.optString("Poster", ""));
			movie.setRuntime(obj.optString("Runtime", ""));
			movie.setRating(obj.optString("Rating", ""));
			movie.setVotes(obj.optString("Votes", ""));

			return movie;
		} catch (final JSONException e) {
			Log.e(TAG, "JSON Parse Exception: " + e.toString());
			return null;
		}
	}

}
