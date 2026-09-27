package ru.ilya.fincontrol;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.view.*;import android.widget.*;import java.text.*;import java.util.*;

public class MainActivity extends Activity {
  LinearLayout root, list; TextView balance, income, expense, debt; android.content.SharedPreferences sp;
  int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);} TextView tv(String s,float z){TextView t=new TextView(this);t.setText(s);t.setTextColor(Color.rgb(244,247,250));t.setTextSize(z);t.setPadding(dp(4),dp(3),dp(4),dp(3));return t;}
  Button btn(String s){Button b=new Button(this);b.setText(s);b.setTextColor(Color.WHITE);b.setAllCaps(false);return b;}
  public void onCreate(Bundle b){super.onCreate(b);sp=getSharedPreferences("f",0);draw();}
  void draw(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(24),dp(18),dp(10));root.setBackgroundColor(Color.rgb(11,15,20));
    TextView title=tv("FinControl",28); title.setTypeface(null,1); root.addView(title,new LinearLayout.LayoutParams(-1,dp(50)));
    LinearLayout cards=new LinearLayout(this); cards.setOrientation(LinearLayout.VERTICAL); root.addView(cards,new LinearLayout.LayoutParams(-1,dp(250)));
    balance=tv("",28); income=tv("",16); expense=tv("",16); debt=tv("",16); cards.addView(balance);cards.addView(income);cards.addView(expense);cards.addView(debt);
    LinearLayout bar=new LinearLayout(this); Button addIn=btn("＋ Доход");Button addOut=btn("− Расход");Button addDebt=btn("Долг");bar.addView(addIn,new LinearLayout.LayoutParams(0,dp(55),1));bar.addView(addOut,new LinearLayout.LayoutParams(0,dp(55),1));bar.addView(addDebt,new LinearLayout.LayoutParams(0,dp(55),1));root.addView(bar); 
    TextView h=tv("Последние операции",20);h.setTypeface(null,1);h.setPadding(0,dp(15),0,dp(5));root.addView(h);
    ScrollView sv=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);sv.addView(list);root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));setContentView(root); refresh();
    addIn.setOnClickListener(v->dialog(true));addOut.setOnClickListener(v->dialog(false));addDebt.setOnClickListener(v->debtDialog());
  }
  void dialog(boolean inc){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL); final EditText a=new EditText(this);a.setHint("Сумма, ₽");a.setInputType(2|8192);final EditText c=new EditText(this);c.setHint("Категория / комментарий");l.addView(a);l.addView(c);new AlertDialog.Builder(this).setTitle(inc?"Добавить доход":"Добавить расход").setView(l).setPositiveButton("Сохранить",(d,w)->{try{long x=Long.parseLong(a.getText().toString());String old=sp.getString("ops","");String line=(inc?"I":"O")+"|"+x+"|"+c.getText()+"|"+System.currentTimeMillis();sp.edit().putString("ops",line+"\n"+old).apply();refresh();}catch(Exception e){}}).setNegativeButton("Отмена",null).show();}
  void debtDialog(){final EditText a=new EditText(this);a.setHint("Сумма долга, ₽");a.setInputType(2|8192);new AlertDialog.Builder(this).setTitle("Обновить общий долг").setView(a).setPositiveButton("Сохранить",(d,w)->{try{sp.edit().putLong("debt",Long.parseLong(a.getText().toString())).apply();refresh();}catch(Exception e){}}).setNegativeButton("Отмена",null).show();}
  void refresh(){long inc=0,out=0;list.removeAllViews();String ops=sp.getString("ops","");for(String line:ops.split("\\n")){if(line.trim().isEmpty())continue;String[] p=line.split("\\|",-1);try{long x=Long.parseLong(p[1]);if(p[0].equals("I"))inc+=x;else out+=x;TextView t=tv((p[0].equals("I")?"+":"-")+" "+money(x)+" ₽   "+p[2],17);t.setPadding(dp(5),dp(12),dp(5),dp(12));list.addView(t);}catch(Exception e){}}long bal=inc-out;balance.setText("Баланс\n"+money(bal)+" ₽");income.setText("Доходы   +"+money(inc)+" ₽");expense.setText("Расходы   -"+money(out)+" ₽");debt.setText("Долги     "+money(sp.getLong("debt",0))+" ₽");}
  String money(long n){return String.format(Locale.US,"%,d",n).replace(',',' ');}
}
