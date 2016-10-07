package rs.manhut.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.*;


/**
 * Created by kulijana on 21.9.16..
 */
public class TRIE {

    static boolean globalCheck;
    static int suggestionCounterLimit=5;
    static int suggestionCounter;
    static int suggestedWord;
    public node root;

    public TRIE () {
        String line;
        this.root = new node();
        try {
            FileReader words = new FileReader("./src/main/resources/words.txt");
            BufferedReader bufferedReader = new BufferedReader(words);
            while ((line = bufferedReader.readLine()) != null) {
                this.root.insertWord(line);
            }
        } catch (Exception e){
            System.out.print("Unable to populate words");
        }
    }

    public Boolean checkWord(String word) {
        try
        {
            Integer number=Integer.parseInt(word);
            return true;
        } catch (Exception e) {
            if (word.equals(".")) {
                return true;
            } else {
                if (this.root.checkForWord(word)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public List<String> suggestWord(String word) {
        List<String> suggested = new ArrayList<String>();
        this.root.suggestWord(word, suggested);
        return suggested;
    }


    static public  class node
    {
        //grane
        private node[] branches = new node[37];
        //provera da li se neka rec ovde zavrsava
        private boolean wordEnd;


        //konstruktor koji po defaultu novu granu namesta kako odgovara
        public node()
        {
            this.wordEnd=false;
        }

        //jednostavna funkcija ciji je cilj nalazenje grane u odnosu na slovo
        private int findLetter(char c)
        {
            switch (c)
            {
                case 'a':
                    return 0;
                case 'b':
                    return 1;
                case 'c':
                    return 2;
                case 'd':
                    return 3;
                case 'e':
                    return 4;
                case 'f':
                    return 5;
                case 'g':
                    return 6;
                case 'h':
                    return 7;
                case 'i':
                    return 8;
                case 'j':
                    return 9;
                case 'k':
                    return 10;
                case 'l':
                    return 11;
                case 'm':
                    return 12;
                case 'n':
                    return 13;
                case 'o':
                    return 14;
                case 'p':
                    return 15;
                case 'q':
                    return 16;
                case 'r':
                    return 17;
                case 's':
                    return 18;
                case 't':
                    return 19;
                case 'u':
                    return 20;
                case 'v':
                    return 21;
                case 'w':
                    return 22;
                case 'x':
                    return 23;
                case 'y':
                    return 24;
                case 'z':
                    return 25;
                case (char) 39:
                    return 26;
                case '0':
                    return 27;
                case '1':
                    return 28;
                case '2':
                    return 29;
                case '3':
                    return 30;
                case '4':
                    return 31;
                case '5':
                    return 32;
                case '6':
                    return 33;
                case '7':
                    return 34;
                case '8':
                    return 35;
                case '9':
                    return 36;

                default:
                    return -1;
            }
        }

        //slican prethodnoj funkciji, samo obrnuto
        private char returnLetter(int i)
        {
            switch (i)
            {
                case 0:
                    return 'a';
                case 1:
                    return 'b';
                case 2:
                    return 'c';
                case 3:
                    return 'd';
                case 4:
                    return 'e';
                case 5:
                    return 'f';
                case 6:
                    return 'g';
                case 7:
                    return 'h';
                case 8:
                    return 'i';
                case 9:
                    return 'j';
                case 10:
                    return 'k';
                case 11:
                    return 'l';
                case 12:
                    return 'm';
                case 13:
                    return 'n';
                case 14:
                    return 'o';
                case 15:
                    return 'p';
                case 16:
                    return 'q';
                case 17:
                    return 'r';
                case 18:
                    return 's';
                case 19:
                    return 't';
                case 20:
                    return 'u';
                case 21:
                    return 'v';
                case 22:
                    return 'w';
                case 23:
                    return 'x';
                case 24:
                    return 'y';
                case 25:
                    return 'z';
                case 26:
                    return (char) 39;
                case 27:
                    return '0';
                case 28:
                    return '1';
                case 29:
                    return '2';
                case 30:
                    return '3';
                case 31:
                    return '4';
                case 32:
                    return '5';
                case 33:
                    return '6';
                case 34:
                    return '7';
                case 35:
                    return '8';
                case 36:
                    return '9';
                default:
                    return '/';
            }
        }
        //funkcija koja se nastavlja na funkciju insertWord, deklarisanu ispod, radi pravo dodavanje reci u TRIE, uz pomoc dodatnih informacija iz prve funkcije
        private void insertWord(String s, int length, node n)
        {
            if(length==0)
            {
                n.wordEnd=true;
            }
            else {
                int a = findLetter(s.charAt(0));
                if (n.branches[a] == null)
                    n.branches[a] = new node();
                insertWord(s.substring(1), length - 1, n.branches[a]);
            }

        }
        //funkcija za unos reci koju koristi user, bez komplikacija, samo se unosi string za unos
        public void insertWord(String s)
        {
            int length= s.length();
            insertWord(s, length, this);
        }
        //funkcija koja proverava da li se rec nalazi u stablu
        private boolean checkForWord(String s,int length, node n)
        {
            if(length==0) {
                if(n.wordEnd==true)
                    return true;
                else
                    return false;
            }
            else {

                int a = findLetter(s.charAt(0));
                if(n.branches[a]==null)
                    return false;
                return checkForWord(s.substring(1), length - 1, n.branches[a]);
            }
        }
        //funkcija koja proverava da li rec postoji, vraca true ili false, user side
        public boolean checkForWord(String s)
        {
            int length = s.length();
            return checkForWord(s,length,this);

        }

        //Proverava da li je grana vezana za vise reci, ako nije, bezbedno je obrisati je
        private int branchCounter(node n)
        {
            int value=0;
            for(int i=0;i<37;i++)
                if(n.branches[i]!=null)
                    value++;
                return value;
        }
        //onaj koji bas brise, poziva se iz deleteWord2 koji se poziva iz public deleteWord
        private void deleteThisNodesBranches(node n)
        {
            for(int i=0;i<37;i++)
            {
                if (n.branches[i]!=null) {
                    deleteThisNodesBranches(n.branches[i]);
                }
                n.branches[i]=null;
            }
        }

        //funkcija koja dubinski ulazi u stablo, i trazi prvu granu koju moze potpuno da izbrise, ukljucijuci i sve njene grane itd.
        private void deleteWord(String s, int length, node n)
        {

            int position;
            if(length==1)
            {
                position=findLetter(s.charAt(0));
                deleteWord("",length-1,n.branches[position]);
            }
            else if(length>0)
            {
                position=findLetter(s.charAt(0));
                deleteWord(s.substring(1), length-1, n.branches[position]);
            }
            if(branchCounter(n)>0 && length==0)
            {
                n.wordEnd=false;
            }
            //naredni if deluje malo konfuzno, sve sto radi jeste da trazi prvu granu koja se ne moze obrisati, zato sto je kraj neke druge reci ili ima ostale reci koje idu iz nje, a globalCheck promenjiva je tu da lomi rekurziju
            if(((n.wordEnd==true && length > 0) || branchCounter(n)>1) && globalCheck==true) {
                position = findLetter(s.charAt(0));
                deleteThisNodesBranches(n);
                n.branches[position]=null;
                globalCheck=false;
            }
        }



        public void deleteWord(String s)
        {
            if(checkForWord(s)==false)
            {
                System.out.println("Word doesn't exist");
            }
            else {
                int length = s.length();
                globalCheck = true;
                deleteWord(s, length, this);
            }
        }
        public void suggestWord(String s, List<String> lista)
        {
            int length = s.length();
            suggestionCounter=0;
            suggestWord(s,s, length, this,lista);
        }

        private void suggestWord(String s, String originalString, int length, node n, List <String> lista)
        {

            if(length!=0)
            {
                int position = findLetter(s.charAt(0));
                suggestWord(s.substring(1),originalString,length-1,n.branches[position], lista);
            }
            else
            {
                if(suggestionCounter<suggestionCounterLimit) {
                    if (n.wordEnd == true) {
                        lista.add(originalString);
                        suggestionCounter++;
                    }
                    for (int i = 0; i < 27; i++) {
                        String helpfulString;
                        if (n.branches[i] != null) {
                            helpfulString= originalString + returnLetter(i);
                            suggestWord(s,helpfulString,0,n.branches[i],lista);

                        }
                    }
                }
            }

        }
    }
}
