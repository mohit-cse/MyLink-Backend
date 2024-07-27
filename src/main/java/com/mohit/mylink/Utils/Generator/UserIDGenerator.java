package com.mohit.mylink.Utils.Generator;

import java.util.Random;
public class UserIDGenerator {
        private final static String CHARACTERS = "Aa0Bb1Cc2Dd3Ee4Ff5Gg6Hh7Ii8Jj9KkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        private final static int SIZE = 62;
        public static String generateId(int length) {
            StringBuilder id = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(SIZE);
                id.append(CHARACTERS.charAt(index));
            }
            return id.toString();
        }
}