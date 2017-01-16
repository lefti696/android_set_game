package com.example.r2d2.gra1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    final Context context = this;
    TextView textElement;

    private static final int[] BUTTON_IDS = {
            R.id.imageButton0,
            R.id.imageButton1,
            R.id.imageButton2,
            R.id.imageButton3,
            R.id.imageButton4,
            R.id.imageButton5,
            R.id.imageButton6,
            R.id.imageButton7,
            R.id.imageButton8,
            R.id.imageButton9,
            R.id.imageButton10,
            R.id.imageButton11,
    };

    Random random = new Random();
    int iloscSetowDoZnalezienia;

    private int[][] cardsInOrder = new int[81][5];
    private int[][] cardsSorted = new int[81][5];     //81 wszystkich kart
    // [ilosc], [red,green,purple], [diamond,zygzag,owal], [filled,empty,cross], [numer obrazka]

    List<List<Integer>> listaSetow = new ArrayList<List<Integer>>();
    List<Integer> wybranySet = new ArrayList<Integer>();
    List<ImageButton> listaWcisnietychButtonow = new ArrayList<ImageButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int rnd;
        int[] tempCard; // na potrzeby sortowania

        final List<Integer> listaObrazkow = new ArrayList<Integer>();
        for(int i = 1; i <= 81; i++){
            listaObrazkow.add(getResources().getIdentifier("obrazek"+i, "drawable", getPackageName()));
        }

        //generowanie wszystkich kart
        for(int i = 0; i < 81; i++){
            cardsInOrder[i][0] = (i / 27);
            cardsInOrder[i][1] = (i % 27 / 9);
            cardsInOrder[i][2] = (i % 9 / 3);
            cardsInOrder[i][3] = (i % 3);
            cardsInOrder[i][4] = i;
        }
//        from source to destination
        System.arraycopy( cardsInOrder, 0, cardsSorted, 0, cardsInOrder.length );

        int c1, c2, c3, lookFor;

        // generowanie ukladu z przynajmniej 3 setami
        while(listaSetow.size() < 6) {
            listaSetow.clear();

            //tasowanie kart algorytmem Knuth-Fisher-Yates
            for (int i = 80; i > 0; i--) {
                rnd = random.nextInt(i + 1);  //do 81
                tempCard = cardsSorted[i];
                cardsSorted[i] = cardsSorted[rnd];
                cardsSorted[rnd] = tempCard;
            }
            // wyswietlenie stolu
//            for(int i = 0; i<12; i++){
//                System.out.format("%d %d %d %d \n", cardsSorted[i][0], cardsSorted[i][1], cardsSorted[i][2], cardsSorted[i][3]);
//            }
            // wyszukanie setow na stole
            for (c1 = 0; c1 < 10; c1++) {
                for (c2 = c1 + 1; c2 < 11; c2++) {
                    for (c3 = c2 + 1; c3 < 12; c3++) {
                        lookFor = (3 - (cardsSorted[c1][0] + cardsSorted[c2][0]) % 3) % 3;
                        if (cardsSorted[c3][0] == lookFor) {
                            // sprawdzam pozostale wlasnosci
                            for (int j = 1; j < 4; j++) {
                                if ((cardsSorted[c1][j] + cardsSorted[c2][j] + cardsSorted[c3][j]) % 3 != 0) break;
                                // jezeli atrybut w 3 kartach nie pasuje to szukaj dalej
                                // w przeciwnym razie set znaleziono
                                if (j == 3) {
                                    // znaleziono set
//                                    System.out.println("znaleziono set");
                                    listaSetow.add(new ArrayList<Integer>());
                                    listaSetow.get(listaSetow.size() - 1).add(cardsSorted[c1][4]);
                                    listaSetow.get(listaSetow.size() - 1).add(cardsSorted[c2][4]);
                                    listaSetow.get(listaSetow.size() - 1).add(cardsSorted[c3][4]);
                                }
                            }
                        }
                    }
                }
            }// koniec for
        }

//        sortuje karty rosnaco dla kazdego setu w liscie setow
        for(int i = 0; i < listaSetow.size(); i++){
            Collections.sort(listaSetow.get(i));
        }
        iloscSetowDoZnalezienia = listaSetow.size();
        Toast.makeText(MainActivity.this,"ilosc setow do znalezienia: " + listaSetow.size(), Toast.LENGTH_LONG).show();

        textElement = (TextView) findViewById(R.id.textView);
        textElement.setText("Found "+ (iloscSetowDoZnalezienia - listaSetow.size()) + " of "+ iloscSetowDoZnalezienia);



//        wygenerowanie 12 kart na ekran
//        for(int i: BUTTON_IDS){   // ale to nie dziala z ustawianiem ikon
        for(int i = 0; i < 12; i++){
            final ImageButton button = (ImageButton) findViewById(BUTTON_IDS[i]);   // BUTTON_IDS[0] = R.id.imageButton0 itd
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    if(listaWcisnietychButtonow.contains(button)){
//                        jesli przycisk byl juz wcisniety
                        button.setAlpha(1f);
//                        odznaczam
                        listaWcisnietychButtonow.remove(listaWcisnietychButtonow.indexOf(button));

                    }
                    else{
//                        jezeli przycisk nie byl wcisniety
                        button.setAlpha(0.5f); // ustawiana przezroczystosc wartosc float pomiedzy 0 a 1
                        listaWcisnietychButtonow.add(button);

                    }

                    if(listaWcisnietychButtonow.size() > 2 ){

//                    wybranySet.get(0) zwraca numer karty

                        // dodaje trzecia karte do setu
                        for(int i = 0; i < 3; i++) {
//                            wczytuje id z trzech zaznaczonych przyciskow i dodaje jako nr karty do wybranego setu
                            wybranySet.add(cardsSorted[listaWcisnietychButtonow.get(i).getId()][4]);
//                            przywracam kolor
                            listaWcisnietychButtonow.get(i).setAlpha(1f);
                        }
                        Collections.sort(wybranySet);   // sortuje wybor
                        listaWcisnietychButtonow.clear();

//                                Toast.makeText(MainActivity.this, wybranySet.get(0) + " " + wybranySet.get(1) + " " + wybranySet.get(2), Toast.LENGTH_SHORT).show();
                        // sprawdzenie czy wybrane 3 karty to set
                        if (cardsInOrder[wybranySet.get(2)][0] == ((3 - (cardsInOrder[wybranySet.get(0)][0] + cardsInOrder[wybranySet.get(1)][0]) % 3) % 3)) {
                            for (int j = 1; j < 4; j++) {
                                if ((cardsInOrder[wybranySet.get(0)][j] + cardsInOrder[wybranySet.get(1)][j] + cardsInOrder[wybranySet.get(2)][j]) % 3 != 0) {
                                    Toast.makeText(MainActivity.this, R.string.brak_seta, Toast.LENGTH_SHORT).show();
                                    switch (j) {
                                        case 1:
                                            Toast.makeText(MainActivity.this, "zla cecha: kolor", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            Toast.makeText(MainActivity.this, "zla cecha: ksztalt", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            Toast.makeText(MainActivity.this, "zla cecha: wypelnienie", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                }
                                if (j == 3) {
                                    Toast.makeText(MainActivity.this, R.string.jest_set, Toast.LENGTH_SHORT).show();
//                                    sprawdzam czy set jest na liscie znalezionych wszystkich setow
                                    if (listaSetow.contains(wybranySet)) {
                                        listaSetow.remove(wybranySet);
                                        if (listaSetow.size() > 0) {
                                            Toast.makeText(MainActivity.this, "pozostalo setow: " + listaSetow.size(), Toast.LENGTH_LONG).show();
                                            textElement.setText("Found " + (iloscSetowDoZnalezienia - listaSetow.size()) + " of " + iloscSetowDoZnalezienia);
                                        } else {
                                            // sekcja popup
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User clicked OK button
                                                    Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                                    MainActivity.this.recreate();
                                                }
                                            });
                                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog
                                                    Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                                                    MainActivity.this.finish();
                                                }
                                            });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Set powtorzony. Pozostalo setow: " + listaSetow.size(), Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                        } else {
                            Toast.makeText(MainActivity.this, "brak seta \n zla cecha: licznosc", Toast.LENGTH_SHORT).show();
                        }

                        wybranySet.clear();

                    }
                } // koniec petli onClick

//                    Toast.makeText(MainActivity.this,"Wybrano karte: " + cardsSorted[button.getId()][4], Toast.LENGTH_SHORT).show();
            });
            button.setId(i);
            button.setImageResource(listaObrazkow.get(cardsSorted[i][4]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);  // new game
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            MainActivity.this.recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
