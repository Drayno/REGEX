package preco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Principal {
	
	private static BufferedReader reader;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Iniciando substituição...");
		List<String> arquivosModificados = new ArrayList<>();
		
		String diretorio = args.length > 0 ? args[0] : null;
		
		if(diretorio == null) {
			throw new FileNotFoundException("É necessário informar um diretório.");
		}
		
		Files.walk(Paths.get(diretorio))
			.filter(file -> file.getFileName().toString().contains(Constantes.EXTENSAO_HTML))
			.forEach(file -> {
				
				try {
					FileReader fileReader = new FileReader(file.toString());
					reader = new BufferedReader(fileReader);
					
					String pagina = montarArquivoHTML(reader);
					HashMap<String, Input> dadosColetados = coletarDadosPorRegex(pagina);
					
					if(!dadosColetados.isEmpty()) {
						System.out.println("");
						System.out.println("Modificando arquivo " + file.getFileName());
						arquivosModificados.add(file.getFileName().toString());
						
						pagina = substituirDadosArquivo(pagina, dadosColetados);
						
						FileUtils.write(new File(file.toString()), pagina, Constantes.UTF8);
						System.out.println(file.getFileName() + " OK");
					}
					
				} catch (FileNotFoundException e) {
					
					System.out.println("Não foi possível acessar o diretório: " + file.toString());
					e.printStackTrace();
					
				} catch (IOException e) {
					
					System.out.println("Falha ao fazer a leitura do aquivo: " + file.getFileName());
					e.printStackTrace();
				}
		});
		
		exibirArquivosModificados(arquivosModificados);
	}

	private static String substituirDadosArquivo(String pagina, HashMap<String, Input> dadosColetados) {
		for(HashMap.Entry<String, Input> input : dadosColetados.entrySet()) {
			String novoComponente = criarAppCorePreco(input);
			
			pagina = pagina.replaceFirst(Pattern.quote(input.getKey()), novoComponente);
		}
		return pagina;
	}

	private static String criarAppCorePreco(HashMap.Entry<String, Input> input) {
		return String.format(getComponentePreco(), 
								input.getValue().getLabel().trim(), 
								input.getValue().getMaxLength().trim(), 
								input.getValue().getPrecision().trim(),
								input.getValue().getMaxInteger().trim(),
								input.getValue().getNgModel().trim());
	}
	
	private static String montarArquivoHTML(BufferedReader reader) throws IOException {
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(Constantes.QUEBRA_LINHA);
		}
		return converterEspacosEmBranco(stringBuilder.toString(), Constantes.ESPACO_ENCODE, Constantes.ESPACO);
	}
	
	
	private static HashMap<String, Input> coletarDadosPorRegex(String texto) {
		Pattern PATTERN = Pattern.compile(obterRegexInput());
        Matcher matcher = PATTERN.matcher(texto);
        HashMap<String, Input> dadosColetados = new HashMap<String, Input>();
        
        while(matcher.find()){
        	dadosColetados.put(matcher.group(1), extraiDadosInput(matcher.group(1)));
        }  
        return dadosColetados;
    }
	
	private static Input extraiDadosInput(String inputString) {
		Input input = new Input();
		
		Pattern PATTERN = Pattern.compile(Constantes.REGEX_MAX_LENGTH);
		Matcher matcher = PATTERN.matcher(inputString);
		input.setMaxLength(matcher.find() ? matcher.group(1)
							.replaceAll(Constantes.ESPACO_ENCODE, Constantes.VAZIO) : Constantes.VAZIO);

		PATTERN = Pattern.compile(Constantes.REGEX_NG_MODEL);
		matcher = PATTERN.matcher(inputString);
		input.setNgModel(matcher.find() ? matcher.group(1)
							.replaceAll(Constantes.ESPACO_ENCODE, Constantes.VAZIO) : Constantes.VAZIO);
		
		PATTERN = Pattern.compile(Constantes.REGEX_PRECISION);
		matcher = PATTERN.matcher(inputString);
		input.setPrecision(matcher.find() ? matcher.group(1)
							.replaceAll(Constantes.DOIS_PONSTOS, Constantes.VAZIO)
							.replaceAll(Constantes.ESPACO_ENCODE, Constantes.VAZIO) : Constantes.VAZIO);
		
		PATTERN = Pattern.compile(Constantes.REGEX_LABEL);
		matcher = PATTERN.matcher(inputString);
		input.setLabel(matcher.find() ? matcher.group(1) : Constantes.VAZIO);
		
		return input;
	}
	
	private static String obterRegexInput() {
		return new StringBuilder()
					.append("(<core-label")
					.append(Constantes.REGEX_INPUT)
					.append("<input")
					.append(Constantes.REGEX_INPUT)
					.append("currencyMask")
					.append(Constantes.REGEX_INPUT)
					.append("</core-label>)")
					.toString();
	}
	
	private static String converterEspacosEmBranco(String dado, String valor, String novoValor) throws UnsupportedEncodingException {
		return new String(dado.toString().getBytes() , Constantes.UTF8).replaceAll(valor, novoValor);
	}
	
	private static String getComponentePreco(){
		return new StringBuilder()
					.append("<app-core-preco #corePreco") 
					.append("	label=\"%s\"") 
					.append("	maxCharacter=\"%s\"") 
					.append("	maxDecimal=\"%s\"")
					.append("	maxInteger=\"%s\"")
					.append("	[valorNumerico]=\"%s\">")
					.append("</app-core-preco>")
					.toString();		
	}
	
	private static void exibirArquivosModificados(List<String> arquivos) {
		if(!arquivos.isEmpty()) {
			System.out.println("");
			System.out.println("===== Arquivos Modificados =====");
			arquivos.forEach(a -> {
				System.out.println(a);
			});
			System.out.println("================================");
		}else {
			System.out.println("Não foi encontrado nenhum arquivo para substituição");
		}
		System.out.println("Substituição finalizada.");
	}

}
