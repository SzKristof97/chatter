package me.szkristof.chatter.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleManager {
    
    /**
     * Létrehoz egy új beolvasót a konzolról.
     * @return A létrehozott beolvasó osztály
     */
    private static BufferedReader GetBufferedReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Kiír egy üzenetet a konzolra
     * @param message Az üzenet
     */
    public static void WriteMessage(String message) {
        System.out.print(message);
    }

    /**
     * Kiír egy üzenetet és bekér egy szöveget a konzolról.
     * @param message Az üzenet
     * @return A beolvasott sor, ha null akkor a beolvasás sikertelen volt
     */
    public static String ReadString(String message) {
        WriteMessage(message);

        BufferedReader reader = GetBufferedReader();
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            WriteMessage("ReadLine error: " + e.getMessage());
        }
        
        return input.trim();
    }

    
    /**
     * Beolvas a konzolról egy számot.
     * 
     * @param message Kiírandó üzenet a konzolra
     * @return A bekért szám, ha -1 akkor a beolvasás sikertelen volt
     */
    public static int ReadInt(String message){
        int input = -1;

        try{
            String sinput = ReadString(message);
            input = Integer.parseInt(sinput);
        }catch(Exception e){
            input = -1;
        }

        return input;
    }

    /**
     * Ellenőrzi, hogy a felhasználó által lenyomott gomb megegyezik a megadott gombbal.
     * @param key A gomb
     */
    public static boolean IsKeyPressed(int keyCode){
        try {
            return System.in.available() > 0 && System.in.read() == keyCode;
        } catch (IOException e) {
            return false;
        }
    }
    /**
     * Beolvas a konzolról egy igaz/hamis értéket.
     * @param message Kiírandó üzenet a konzolra
     * @return Igaz ha igaz/i, hamis ha hamis/h
     */
    public static boolean ReadBoolean(String message, String trueString, String falseString){
        boolean input = false;

        try{
            String sinput = ReadString(message);
            if(sinput.toLowerCase().equals(trueString.toLowerCase())){
                input = true;
            }else if(sinput.toLowerCase().equals(falseString.toLowerCase())){
                input = false;
            }else{
                input = false;
            }
        }catch(Exception e){
            input = false;
        }

        return input;
    }

    /**
     * Törli a képernyőt
     */
    public static void Clear() {
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.out.println("Failed to clear console.");
            }
        } else {
            try {
                Runtime.getRuntime().exec("clear");
            } catch (Exception e) {
                System.out.println("Failed to clear console.");
            }
        }

        System.out.flush();
    }
}
