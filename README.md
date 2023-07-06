# Evitar exibiçao e clicks fraudulentos no admob

## AS VARIAVEIS

<code>
    //CLICKNOFRAUD: VARIAVEL ARQUIVO XML QUE SALVA OS DADOS E OUTRAS
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
    //CLICKNOFRAUD: VARIAVEL ARQUIVO XML QUE SALVA OS DADOS E OUTRAS
</code>
