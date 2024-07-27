package com.mohit.mylink.Utils;

import java.util.Random;
public class UserIDGenerator {
        private final static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
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