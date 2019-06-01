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
        String resp = new String(pctVeio.getData());
        String[] arrayResposta = resp.split("[;]");
        String respostaServidor = "";
        int x = 2;
        int countAcertos = 0;
        int countErros = 0;
        for(int i=0;i<quantidadePerguntas;i++){
            
            char [] charArrayGabarito = arrayGabarito[x].toCharArray();
            char [] charArrayResposta = arrayResposta[x].toCharArray();

            for(int z=0;z<charArrayGabarito.length;z++){

                if( charArrayGabarito[z] == charArrayResposta[z]){
                    countAcertos = countAcertos+1;
                }else{
                    countErros = countErros+1;
                }
            }
            x = x+3;
            respostaServidor += i+1 + ";" + countAcertos + ";" + countErros + ";";
            countAcertos = 0;
            countErros = 0;
        }
        
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