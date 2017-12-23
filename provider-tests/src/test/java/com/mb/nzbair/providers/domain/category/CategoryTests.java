package com.mb.nzbair.providers.domain.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CategoryTests {

	RootCategory Anime;
	RootCategory Apps;
	RootCategory Documentaries;
	RootCategory Games;
	RootCategory Movies;
	RootCategory Music;
	RootCategory Other;
	RootCategory TV;

	@Before
	public void setup() {

		List<Category> categories = new ArrayList<Category>();

		Anime = new RootCategory("100", "Anime");
		Apps = new RootCategory("101", "Apps");
		Documentaries = new RootCategory("102", "Documentaries");
		Games = new RootCategory("103", "Games");
		Movies = new RootCategory("104", "Movies");
		Music = new RootCategory("105", "Music");
		Other = new RootCategory("106", "Other");
		TV = new RootCategory("107", "TV");
		// Set siblings
		Anime.setSiblings(categories);
		Apps.setSiblings(categories);
		Documentaries.setSiblings(categories);
		Games.setSiblings(categories);
		Movies.setSiblings(categories);
		Music.setSiblings(categories);
		Other.setSiblings(categories);
		TV.setSiblings(categories);

		categories.add(new Category("Latest"));
		categories.add(Anime);
		categories.add(Apps);
		categories.add(Documentaries);
		categories.add(Games);
		categories.add(Movies);
		categories.add(Music);
		categories.add(Other);
		categories.add(TV);
		categories.add(new FavouritesCategory("", "Favourites"));

		Anime.addChild(new ParentCategory());
		Anime.addChild(new Category("28", "All"));
		Apps.addChild(new ParentCategory());
		Apps.addChild(new BranchCategory("201", "All"));
		Apps.addChild(new Category("20", "Apps : Linux"));
		Apps.addChild(new Category("19", "Apps : Mac"));
		Apps.addChild(new Category("21", "Apps : Other"));
		Apps.addChild(new Category("18", "Apps : PC"));
		Apps.addChild(new Category("55", "Apps : Phone"));
		Apps.addChild(new Category("52", "Apps : Portable"));
		Documentaries.addChild(new ParentCategory());
		Documentaries.addChild(new BranchCategory("202", "All"));
		Documentaries.addChild(new Category("53", "Documentaries : HD"));
		Documentaries.addChild(new Category("9", "Documentaries : STD"));
		Games.addChild(new ParentCategory());
		Games.addChild(new BranchCategory("203", "All"));
		Games.addChild(new Category("16", "Games : Dreamcast"));
		Games.addChild(new Category("45", "Games : DS"));
		Games.addChild(new Category("46", "Games : GameCube"));
		Games.addChild(new Category("17", "Games : Other"));
		Games.addChild(new Category("10", "Games : PC"));
		Games.addChild(new Category("15", "Games : PS1"));
		Games.addChild(new Category("11", "Games : PS2"));
		Games.addChild(new Category("43", "Games : PS3"));
		Games.addChild(new Category("12", "Games : PSP"));
		Games.addChild(new Category("44", "Games : Wii"));
		Games.addChild(new Category("51", "Games : Wii VC"));
		Games.addChild(new Category("13", "Games : Xbox"));
		Games.addChild(new Category("14", "Games : Xbox360"));
		Games.addChild(new Category("56", "Games : Xbox360 (Other)"));
		Movies.addChild(new ParentCategory());
		Movies.addChild(new BranchCategory("204", "All"));
		Movies.addChild(new Category("54", "Movies : BRRip"));
		Movies.addChild(new Category("2", "Movies : Divx"));
		Movies.addChild(new Category("1", "Movies : DVD"));
		Movies.addChild(new Category("50", "Movies : HD (Image)"));
		Movies.addChild(new Category("42", "Movies : HD (x264)"));
		Movies.addChild(new Category("4", "Movies : Other"));
		Movies.addChild(new Category("3", "Movies : SVCD"));
		Movies.addChild(new Category("48", "Movies : WMV-HD"));
		Music.addChild(new ParentCategory());
		Music.addChild(new BranchCategory("205", "All"));
		Music.addChild(new Category("24", "Music : DVD"));
		Music.addChild(new Category("23", "Music : Lossless"));
		Music.addChild(new Category("22", "Music : MP3 Albums"));
		Music.addChild(new Category("47", "Music : MP3 Singles"));
		Music.addChild(new Category("27", "Music : Other"));
		Music.addChild(new Category("25", "Music : Video"));
		Other.addChild(new ParentCategory());
		Other.addChild(new BranchCategory("206", "All"));
		Other.addChild(new Category("49", "Other : Audio Books"));
		Other.addChild(new Category("36", "Other : E-Books"));
		Other.addChild(new Category("33", "Other : Emulation"));
		Other.addChild(new Category("39", "Other : Extra Pars"));
		Other.addChild(new Category("37", "Other : Images"));
		Other.addChild(new Category("38", "Other : Mobile Phone"));
		Other.addChild(new Category("40", "Other : Other"));
		Other.addChild(new Category("34", "Other : PPC"));
		Other.addChild(new Category("26", "Other : Radio"));
		TV.addChild(new ParentCategory());
		TV.addChild(new BranchCategory("207", "All"));
		TV.addChild(new Category("6", "TV : Divx"));
		TV.addChild(new Category("5", "TV : DVD"));
		TV.addChild(new Category("41", "TV : HD"));
		TV.addChild(new Category("8", "TV : Other"));
		TV.addChild(new Category("7", "TV : Sport"));
	}

	@Test
	public void testHasChildren() {
		assertTrue(Anime.hasChildren());
	}

	@Test
	public void testNotHasChildren() {
		assertFalse((new Category("7", "TV : Sport")).hasChildren());
	}

	@Test
	public void testHasParent() {
		assertTrue(TV.getChildren().get(3).hasParent());
	}

	@Test
	public void testNotHasParent() {
		assertFalse(TV.hasParent());
	}

	@Test
	public void testGetChildren() {
		assertEquals(7, TV.getChildren().size());
	}

	@Test
	public void testAddChildren() {
		Category sport = new Category("7", "TV : Sport");
		Category sport2 = new Category("8", "TV : Sports");
		sport.setProvider("test");
		sport.addChild(sport2);
		assertEquals(sport, sport2.getParent());
		assertEquals("test", sport2.getProviderId());

	}

	@Test
	public void testAddChildrenWithSiblings() {
		Category sport = new Category("7", "TV : Sport");
		sport.setProvider("test");

		List<Category> siblings = new ArrayList<Category>();
		Category sport2 = new Category("8", "TV : Sports");
		Category sport3 = new Category("9", "TV : Sports4");

		siblings.add(sport2);
		siblings.add(sport3);

		sport.addChild(sport2);
		sport.addChild(sport3);

		assertEquals(2, sport2.getSiblings().size());
	}

	@Test
	public void testGenerateIdsAsCSV() {
		assertEquals("6,5,41,8,7", TV.generateCategoryIdsAsCSV());
	}
}
