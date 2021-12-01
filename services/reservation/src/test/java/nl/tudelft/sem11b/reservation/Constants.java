package nl.tudelft.sem11b.reservation;

public class Constants {
    public static String jsonResponse = "{\n" +
            "  \"suffix\": \"LH-AMP\",\n" +
            "  \"name\": \"Lecture Hall Ampére\",\n" +
            "  \"building\": {\n" +
            "    \"id\": 64,\n" +
            "    \"prefix\": \"EWI\",\n" +
            "    \"name\": \"Faculty building of Electronic Engineering, Mathematics and Computer Science\",\n" +
            "    \"open\": \"07:00\",\n" +
            "    \"close\": \"20:00\"\n" +
            "  },\n" +
            "  \"capacity\": 329,\n" +
            "  \"equipment\": [\n" +
            "    {\n" +
            "      \"id\": 35,\n" +
            "      \"name\": \"Desktop PC\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 78,\n" +
            "      \"name\": \"Smartboard\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"closure\": {\n" +
            "    \"reason\": \"Maintenance\",\n" +
            "    \"since\": \"2022-06-12\",\n" +
            "    \"until\": \"2022-06-20\"\n" +
            "  }\n" +
            "}";

    public static String jsonResponseNoClosure = "{\n" +
            "  \"suffix\": \"LH-AMP\",\n" +
            "  \"name\": \"Lecture Hall Ampére\",\n" +
            "  \"building\": {\n" +
            "    \"id\": 64,\n" +
            "    \"prefix\": \"EWI\",\n" +
            "    \"name\": \"Faculty building of Electronic Engineering, Mathematics and Computer Science\",\n" +
            "    \"open\": \"07:00\",\n" +
            "    \"close\": \"20:00\"\n" +
            "  },\n" +
            "  \"capacity\": 329,\n" +
            "  \"equipment\": [\n" +
            "    {\n" +
            "      \"id\": 35,\n" +
            "      \"name\": \"Desktop PC\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 78,\n" +
            "      \"name\": \"Smartboard\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static String jsonResponseNullClosure = "{\n" +
            "  \"suffix\": \"LH-AMP\",\n" +
            "  \"name\": \"Lecture Hall Ampére\",\n" +
            "  \"building\": {\n" +
            "    \"id\": 64,\n" +
            "    \"prefix\": \"EWI\",\n" +
            "    \"name\": \"Faculty building of Electronic Engineering, Mathematics and Computer Science\",\n" +
            "    \"open\": \"07:00\",\n" +
            "    \"close\": \"20:00\"\n" +
            "  },\n" +
            "  \"capacity\": 329,\n" +
            "  \"equipment\": [\n" +
            "    {\n" +
            "      \"id\": 35,\n" +
            "      \"name\": \"Desktop PC\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 78,\n" +
            "      \"name\": \"Smartboard\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"closure\": null\n" +
            "}";
}
