import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Scanner;  
import java.io.IOException;
import java.net.InetAddress;


public class CorrigirProvasClient {

    public static void main(String[] args) throws Exception{
		DatagramSocket socket;
		int portaDestino = 6789;
		Scanner input = new Scanner(System.in);
		
        System.out.print("Informe a quantidade de perguntas: ");
		int quantidadePerguntas = input.nextInt();			
		Respostas[] teste = new Respostas[quantidadePerguntas];

		for(int i=0;i<quantidadePerguntas;i++){
				teste[i] = new Respostas();	
				teste[i].setPergunta(i+1);
			System.out.printf("\nInforme o numero de alternativas para a questao %d: ", i+1);
                teste[i].setNumAlternativas(input.nextInt());
			
			System.out.print("\nInforme a resposta:  ");
				teste[i].setResposta(input.next());					
        }

		String enviandoPerguntas = "";
		for(int k=0;k<quantidadePerguntas;k++){
		    enviandoPerguntas += teste[k].getPergunta() + ";" + teste[k].getNumAlternativas() + ";" + teste[k].getResposta() + ";";
		}
		System.out.println("Visualizando o pacote no formato correto: " + enviandoPerguntas);

		try {
            socket = new DatagramSocket();
            byte[] message = enviandoPerguntas.getBytes();
            InetAddress host = InetAddress.getByName("localhost");
            
            DatagramPacket pctVai = new DatagramPacket(message, message.length, host, portaDestino);
            socket.setSoTimeout(30000);
            socket.send(pctVai);
            System.out.println("Prova enviada com sucesso");
            
            byte[] buffer = new byte[1024];
            DatagramPacket pctVeio = new DatagramPacket(buffer, buffer.length);
            socket.receive(pctVeio);
            Thread.sleep(10000);
            System.out.println("Resposta:" + new String(pctVeio.getData()).trim());
            socket.close();
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}