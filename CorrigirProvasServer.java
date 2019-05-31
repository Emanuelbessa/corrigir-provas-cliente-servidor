import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner; 

public class CorrigirProvasServer {

    public static void main(String[] args) {
        int porta = 6789;
        DatagramSocket socket;
        Scanner input = new Scanner(System.in);

        System.out.print("Informe o gabarito da prova\n");
        System.out.print("Informe a quantidade de perguntas: ");
		int quantidadePerguntas = input.nextInt();			
		Respostas[] gabarito = new Respostas[quantidadePerguntas];

        String enviandoGabarito = "";
		for(int y=0;y<quantidadePerguntas;y++){
				gabarito[y] = new Respostas();	
				gabarito[y].setPergunta(y+1);
			System.out.printf("\nInforme o numero de alternativas para a questao %d: ", y+1);
                gabarito[y].setNumAlternativas(input.nextInt());
			
			System.out.print("\nInforme a resposta:  ");
                gabarito[y].setResposta(input.next());
            
            enviandoGabarito += gabarito[y].getPergunta() + ";" + gabarito[y].getNumAlternativas() + ";" + gabarito[y].getResposta() + ";";
        }


        try {
            socket = new DatagramSocket(porta);

            System.out.printf("Servidor online e aguardando por cliente na porta %d!\n", socket.getLocalPort());
            byte[] buffer = new byte[1024];
            ArrayList<CorrigirProvasThread> threads = new ArrayList<>();

            while (true) {
                DatagramPacket pctVeio = new DatagramPacket(buffer, buffer.length);
                socket.receive(pctVeio);
                System.out.println("Requisição recebida do cliente: " + pctVeio.getAddress());

                CorrigirProvasThread thread = new CorrigirProvasThread(pctVeio, enviandoGabarito, quantidadePerguntas);
                threads.add(thread);
                thread.start();
            }
        } catch (SocketException e) {
            System.err.println("Erro no Socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de IO: " + e.getMessage());
        }

        
        
    }
}