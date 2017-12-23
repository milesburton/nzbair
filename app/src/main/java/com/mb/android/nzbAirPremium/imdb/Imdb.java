
package com.mb.android.nzbAirPremium.imdb;

import java.io.Serializable;

/*
 { "Title":"Lone Wolf and Cub: Baby Cart in Peril", "Year":"1972",
 "Rated":"Unrated", "Released":"Nov 1972", "Genre":"Action",
 "Director":"Buichi Saito", "Writer":"Kazuo Koike, Goseki Kojima",
 "Actors":"Tomisaburo Wakayama, Yoichi Hayashi, Michie Azuma, Akihiro
 Tomikawa", "Plot":"Forth film in the Lone Wolf and Cub series. Ogami
 is hired to kill a tattooed female assassin. Gunbei Yagyu...",
 "Poster":"http://ia.media-imdb.com/images/M/MV5BMTQ2MzQyOTI3NV5BMl5BanBnXkFtZTcwNTEwNTUyMQ@@._V1._SX320.jpg",
 "Runtime":"1 hr 21 mins", "Rating":"7.7", "Votes":"1213",
 "ID":"tt0143348", "Response":"True" }
 */
public class Imdb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6778831568857684401L;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getVotes() {
		return votes;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}

	private String title, year, rated, released, genre, director, writer, actors, plot, image, runtime, rating, votes, id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return "<a href=\"http://www.imdb.com/title/tt" + getId() + "\">IMDB Online Details</a>";
	}
}
