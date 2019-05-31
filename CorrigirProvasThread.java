import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class CorrigirProvasThread extends Thread {    
    private final DatagramPacket pctVeio;
    private final String gabarito;
    private final int quantidadePerguntas;

    public CorrigirProvasThread(DatagramPacket pctVeio, String gabarito, int quantidadePerguntas) {
        this.pctVeio = pctVeio;
        this.gabarito = gabarito;
        this.quantidadePerguntas = quantidadePerguntas;
    }


    
    @Override
    public void run(){
        String[] arrayGabarito = gabarito.split("[;]");
        String teste = new String(pctVeio.getData());
        String[] arrayResposta = teste.split("[;]");
        //String[] arrayResposta = pctVeio.getData().toString().split("[;]");

        String respostaServidor = "";
        int x = 0;
        int countAcertos = 0;
        int countErros = 0;
        for(int i=0;i<quantidadePerguntas;i++){
            //1;5;VVFFV;2;4;VVVV; - gab
            //1;5;VVVVV;2;4;FFVV; - resp
            char [] charArrayGabarito = arrayGabarito[x+2].toCharArray();
            char [] charArrayResposta = arrayResposta[x+2].toCharArray();

            for(int z=0;z<charArrayGabarito.length;z++){

                if( charArrayGabarito[z] == charArrayResposta[z]){
                    countAcertos = countAcertos+1;
                }else{
                    countErros = countErros+1;
                }
            }
            x = i+3;
            respostaServidor += i+1 + ";" + countAcertos + ";" + countErros + ";";
            countAcertos = 0;
            countErros = 0;
        }

        

        //respostaServidor
        //EstatisticaProvasThread thread = new EstatisticaProvasThread (respostaServidor);
        //String testandoEstat = respostaServidor;
        byte[] msgVai = respostaServidor.getBytes();
        DatagramPacket pctVai = new DatagramPacket(msgVai, msgVai.length, pctVeio.getAddress(), pctVeio.getPort());
        
        try {
            DatagramSocket destino = new DatagramSocket();
            destino.send(pctVai);
            destino.close();
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}