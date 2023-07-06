# Evitar exibiçao e clicks fraudulentos no admob

## AS VARIAVEIS

```
    
    private static final  String DB_XML = "NOME_DB_APP"; // nome do banco de dados
    private static final int carrementoLimite =  3; // limite de carregamentos
    private static final int clickLimite = 3; // limite de clicks
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    int carregamento;
    int click;
    int GetClick;
    String GetData;
    String Data;
    
```
## COLOQUE DEPOIS DO ONCREATE

```

        preferences = getSharedPreferences(DB_XML,0);
        editor = preferences.edit();
        GetClick = preferences.getInt("GetClick", 0);
        GetData = preferences.getString("GetData", "00/00");
        
```
## COLOQUE COMO MEDOTODO DA CLASSE

```

    private void VezesProcessada() {
        carregamento += 1;
        if (carregamento >= carrementoLimite && GetClick <= clickLimite) {
            showInterstitial();
            carregamento = 0;
        }else if(GetClick > clickLimite){
            ResetClicks();
        }
    }
    public void ClickNoFraud(){
        Date dataHoraAtual = new Date();
        Data = new SimpleDateFormat("dd/MM").format(dataHoraAtual);
        click = click +1;
        editor.putInt("GetClick", click);
        editor.putString("GetData", Data);
        editor.commit();
        GetClick = preferences.getInt("GetClick", 0);
        GetData = preferences.getString("GetData", "00/00");
        TextViews    = (TextView) findViewById(R.id.TextView);
        TextViews.setText("GetClick: " + GetClick + " GetDatas: " + GetData);
    }
    public void ResetClicks(){
        Date dataHoraAtual = new Date();
        Data = new SimpleDateFormat("dd/MM").format(dataHoraAtual);
        GetData = preferences.getString("GetData", "00/00");
        if(!Objects.equals(GetData, Data)){
            click = 0;
            Data = "00/00";
            editor.putInt("GetClick", click);
            editor.putString("GetData", Data);
            editor.commit();
            GetClick = preferences.getInt("GetClick", 0);
            GetData = preferences.getString("GetData", "00/00");
            TextViews.setText("GetClick: " + GetClick + " GetDatas: " + GetData);
        }
    }
        
```
## COLOQUE DENTRO DO LOAD - onAdClicked

```

        ClickNoFraud();
        
```
