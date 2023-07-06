# Evitar exibiçao e clicks fraudulentos no admob

## AS VARIAVEIS

```
    
    private static final  String DB_XML = "NOME_DB_APP"; // nome do banco de dados
    private static final int visualizacoesLimite =  3; // limite de carregamentos
    private static final int clickLimite = 3; // limite de clicks
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    int visualizacoes; // visualizacoesLimite é igual a zero quando inicia
    int click; // clicks é igual a zero quando inicia
    int GetClick; // salva clicks
    String GetData; // salva datas
    String Data; // data inicial
    
```
## COLOQUE DEPOIS DO ONCREATE

```

        preferences = getSharedPreferences(DB_XML,0); //chama aqruivo que salva quantidades
        editor = preferences.edit(); // chama o editor do aquivo db
        GetClick = preferences.getInt("GetClick", 0); // pega a quantidade de clicks salvos
        GetData = preferences.getString("GetData", "00/00"); // pega a data salva
        
```
## COLOQUE COMO MEDOTODO DA CLASSE

```

    private void VisualizacoesProcessadas() {
        visualizacoes += 1; // metodo para exibir deposi de quantidade visualizada
        if (visualizacoes >= visualizacoesLimite && GetClick <= clickLimite) {
            showInterstitial(); // metodo para chamar os anuncios
            visualizacoes = 0;
        }else if(GetClick > clickLimite){
            ResetClicks(); // medoto para resetar os clicks pela data
        }
    }
    public void ClickNoFraud(){
        Date dataHoraAtual = new Date(); // classe data
        Data = new SimpleDateFormat("dd/MM").format(dataHoraAtual);
        click = click +1; // quantidade de clicks
        editor.putInt("GetClick", click); // salva os clicks
        editor.putString("GetData", Data); // salva as datas
        editor.commit();
        GetClick = preferences.getInt("GetClick", 0); // chama o novo click
        GetData = preferences.getString("GetData", "00/00"); // chama a nova data
    }
    public void ResetClicks(){
        Date dataHoraAtual = new Date(); // classe data
        Data = new SimpleDateFormat("dd/MM").format(dataHoraAtual);
        GetData = preferences.getString("GetData", "00/00"); // pega a data salva
        if(!Objects.equals(GetData, Data)){
            click = 0; // reseta para 0 clicks
            Data = "00/00"; // reseta para 0  datas
            editor.putInt("GetClick", click); // salva para 0 clicks
            editor.putString("GetData", Data); // salva para 0 datas
            editor.commit();
            GetClick = preferences.getInt("GetClick", 0); // chama novamente clicks
            GetData = preferences.getString("GetData", "00/00"); // chama novamente datas
        }
    }
        
```
## COLOQUE DENTRO DO LOAD - onAdClicked

```

        ClickNoFraud();
        
```
## COLOQUE DENTRO DA AÇAO PARA CHAMAR O PROCESSAMENTO

```

        VisualizacoesProcessadas();
        
```

