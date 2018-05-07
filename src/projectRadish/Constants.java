package projectRadish;


import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import net.dv8tion.jda.core.OnlineStatus;

public final class Constants {
	
	private static Set<String> RadishAdmin = createRadishAdmin();
	private static Set<String> createRadishAdmin() {
		Set<String> idList = new HashSet<>();
		idList.add("173001519715581952");
		idList.add("259865752553783297");
		idList.add("100903082652602368");
		return idList;
	}
	public static Set<String> getRadishAdmin(){
		return RadishAdmin;
	}
	
	private static Set<String> TPEAdmin = createTPEAdmin();
	private static Set<String> createTPEAdmin(){
		Set<String> idList = new HashSet<>();
		idList.add("183006569410068480");
		return idList;
	}
	public static Set<String> getTPEAdmin(){
		return TPEAdmin;
	}
	

	private static final String BotToken = "NDMxNjcwNDI3NDg0Njg0MzEx.DctCUg.ziZXxz5YefmK_qGFyssiU8_qLP4";
	public static String getBotToken() { return BotToken; }


	private static String[] currentDoc = {"Metroid Prime",
	        "https://docs.google.com/document/d/1xXTHVRa66YQIxCOseaiD1ga_5IlTL-_CD8b9GW2bKzs/edit"};
	public static String[] getCurrentDoc() { return currentDoc; }


	private static HashMap<String, String> docs = createDocMap();
	private static HashMap<String, String> createDocMap()	// Populate Mapping
	{
		HashMap<String,String> docs = new HashMap<>();
		docs.put("TPPM Original", "https://docs.google.com/document/d/1pm06Nq6v1VGYTeqn9TyzD6WKjOs7MQ5qKJW5w9-vOCE/preview");
		docs.put("The Thousand Year Door", "https://docs.google.com/document/d/1x_wNw2v242yKm25pXuKKsDTKL8Qtf-pUm6DccLfYQuE/preview");
		docs.put("A Link to the Past", "https://docs.google.com/document/d/18dnKqdNY12LitbJ7qPgCZLo39dH-9gJrDsDBb8s2-Ag/preview");
		docs.put("Donkey Kong Country Incomplete", "https://docs.google.com/document/d/1-daCxM-mIrPXuW4Y_miHXnnafHpzNTl5IogwL_2FsEo/preview");
		docs.put("EarthBound", "https://docs.google.com/document/d/18oC6WYg3H4SkaYpjWAMksqUTWo8aKo67MTzBSkRLvrQ/preview");
		docs.put("Wind Waker", "https://docs.google.com/document/d/1DuLW4JsIwHvLlLTAOrsg-_z4c1oDy5l0LuAG5On5e18/preview");
		docs.put("Super Mario Sunshine", "https://docs.google.com/document/d/1ObwfTn5R85pROaCiTLIhtS0vr1sBKg2TDb46Io6-lY8/preview");
		docs.put("Super Mario Sunshine: Blue Coins", "https://docs.google.com/document/d/1ttyViODNXvLaPxOdmPzV5xkcfbe7liAqGGDoXTi4WrM/preview");
		docs.put("Ocarina of Time", "https://docs.google.com/document/d/1tz91cBVdu0PJT7iTc8yTi629-bJmfJU0g88zgAbe0ac/preview");
		docs.put("Super Mario 64", "https://docs.google.com/document/d/1MRp8kHmQYE1mHq6c2LSZIP6p0CYBLz39jg_cgP5TmZk/preview");
		docs.put("Kirby's Epic Yarn", "https://docs.google.com/document/d/1UALBhBnfw5Ay2yJpb2QIsnXZ1uYkGSgMfD43-R5dnEA/preview");
		docs.put("Banjo Tooie", "https://docs.google.com/document/d/182eviZvNivH0samlt5PfL2ZVnhqlf5NRVc4pkLAo2m8/preview");
		docs.put("Banjo Kazooie", "https://docs.google.com/document/d/1BS3ypZjcivxkW5EtFJx3Rzj0EDKVzOUF4UIKvTquWf8/preview");
		docs.put("Yoshi's Story", "https://docs.google.com/document/d/1MV0OMXJ8NFjXq6PFZnoRAPF53f_k7PGEKzs-Ll4lxsg/preview");
		docs.put("Majora's Mask", "https://docs.google.com/document/d/1sI2JjEYfWwwSGU4PiYaKmFp5hPw9TbmsVoukwGS18T4/preview");
		docs.put("Luigi's Mansion", "https://docs.google.com/document/d/1LSz7U7axdJeFvXuCyxns13FBhpdAccVD7DMbUQYb5fA/preview");
		docs.put("Super Mario RPG", "https://docs.google.com/document/d/1GssMt4cdwXKTHwcpZ75Mz26B5e8xw4OrKjpAQMHTMFk/preview");
		docs.put("Super Paper Mario", "https://docs.google.com/document/d/1HRwpGPzhTSg8OqO6ZXGcpnonrAMa8n2svxOh4jURNHo/preview");
		docs.put("The Thousand Year Door: Hard Mode", "https://docs.google.com/document/d/1nswHp3xCQPPioINOPidj3o5mWgCwngLRSSUiy70upMA/preview");
		docs.put("The Thousand Year Door #2", "https://docs.google.com/document/d/1-6q8-uzvobHqtqxCxNoSQbJ603386HgBsNLwNXShh7M/preview");
		docs.put("Paper Mario", "https://docs.google.com/document/d/1-zRbVid0LR4-n3smOzqjLYUsIIzUSvmZxvOg0mHKRq0/preview");
		docs.put("Super Mario Galaxy", "https://docs.google.com/document/d/1KrK7c6EehC__rE6keDb41Agv_njqy3QyvqkxQ2cE5Mo/preview");
		docs.put("Superstar Saga", "https://docs.google.com/document/d/1eM2jQUMICdx7wBQtsFisqdNQjqEtBxRhLWtZElmOAIA/preview");
		docs.put("Donkey Kong Country", "https://docs.google.com/document/d/1Na-GznWfqfygOUWE-j89ewkSvE6pQBJkEGBda5UaqpA/preview");
		docs.put("FireRed/LeafGreen", "https://docs.google.com/document/d/1m4y-SnXsFtovKQSDkNogzxbM_bbbGsrG-47Ge5QwVK8/preview");
		docs.put("Yoshi's Island", "https://docs.google.com/document/d/15HRFa2iRkVGeD6IokyPT08Qn14SfhiaHXkR6GejbMSA/preview");
		docs.put("Metroid Prime", "https://docs.google.com/document/d/1xXTHVRa66YQIxCOseaiD1ga_5IlTL-_CD8b9GW2bKzs/preview");
		return docs;
	}
	public static HashMap<String, String> getDocs() { return docs; }

	//private static final String AdminRoleID = "441484541899833355"; not needed for now
	
	private static String defaultGame = "VRchat"; // Set to null for no initial game
	public static String getDefaultGame() { return defaultGame; }

	private static OnlineStatus defaultStatus = OnlineStatus.DO_NOT_DISTURB;
	public static OnlineStatus getDefaultStatus() { return defaultStatus; }


	// Private Constructor to prevent instantiation
	private Constants() {}
}
