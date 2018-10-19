package preco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Principal {

	private static final String QUEBRA_LINHA = "\n";
	private static final String UTF8 = "UTF-8";
	private static final String ESPACO_ENCODE = "\u00A0";
	private static final String ESPACO = " ";
	
	private static final String REGEX_INPUT = "([a-z]|[A-Z]|\\d|\"|\'|:|,|:|@|#|\\$|%|\\(|\\)|\\[|\\]|\\{|\\}|\\||\\.| |=|\\/|\n|\\u00A0|!)*";
	private static final String REGEX_MAX_LENGTH = "maxlength.*\"(.*)\"";
	private static final String REGEX_PRECISION = "precision(.*)}";
	private static final String REGEX_NG_MODEL = "\\[\\(ngModel\\)\\].*\"(.*)\"";
	
	private static final String DIRETORIO = "C:\\Dev\\Projetos\\TEMA\\workspace-pricing\\BPPricing\\pricing-web-ng\\pricing-web-ng-core\\src\\app\\componentes\\ativos\\titulo\\novo-titulo\\amortizacao\\AmortizacaoComponent.html";
	private static BufferedReader reader;
	
	public static void main(String[] args) throws IOException {

		FileReader file = new FileReader(DIRETORIO);
		reader = new BufferedReader(file);
		
		String data = montarArquivoHTML(reader);
		System.out.println(data);
		
		HashMap<String, Input> dadosColetados = coletarDadosPorRegex(data);
		
		for(HashMap.Entry<String, Input> input : dadosColetados.entrySet()) {
		   System.out.println(String.format(getComponentePreco(), 
				   							input.getValue().getMaxLength(), 
				   							input.getValue().getPrecision(),
				   							input.getValue().getNgModel()));
		}
	}
	
	private static String montarArquivoHTML(BufferedReader reader) throws IOException {
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(QUEBRA_LINHA);
		}
		
		return converterEspacosEmBranco(stringBuilder);
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
		
		Pattern PATTERN = Pattern.compile(REGEX_MAX_LENGTH);
		Matcher matcher = PATTERN.matcher(inputString);
		input.setMaxLength(matcher.find() ? matcher.group(1).replaceAll(ESPACO_ENCODE, "") : "");

		PATTERN = Pattern.compile(REGEX_NG_MODEL);
		matcher = PATTERN.matcher(inputString);
		input.setNgModel(matcher.find() ? matcher.group(1).replaceAll(ESPACO_ENCODE, "") : "");
		
		PATTERN = Pattern.compile(REGEX_PRECISION);
		matcher = PATTERN.matcher(inputString);
		input.setPrecision(matcher.find() ? matcher.group(1).replaceAll(":", "").replaceAll(ESPACO_ENCODE, "") : "");
		
		return input;
	}
	
	private static String obterRegexInput() {
		return new StringBuilder()
					.append("(<input")
					.append(REGEX_INPUT)
					.append("currencyMask")
					.append(REGEX_INPUT)
					.append(">)")
					.toString();
	}
	
	private static String converterEspacosEmBranco(StringBuilder stringBuilder) throws UnsupportedEncodingException {
		return new String(stringBuilder.toString().getBytes() , UTF8).replaceAll(ESPACO, ESPACO_ENCODE);
	}
	
	private static String getComponentePreco(){
		return new StringBuilder()
					.append("<app-core-preco #corePreco") 
					.append("	maxCharacter=\"%s\"") 
					.append("	maxDecimal=\"%s\"")
					.append("	[valorNumerico]=\"%s\">")
					.append("</app-core-preco>")
					.toString();
	}

}
