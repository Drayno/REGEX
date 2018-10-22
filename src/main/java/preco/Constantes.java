package preco;

public class Constantes {
	
	public static final String QUEBRA_LINHA = "\n";
	public static final String UTF8 = "UTF-8";
	public static final String ESPACO_ENCODE = "\u00A0";
	public static final String ESPACO = " ";
	public static final String EXTENSAO_HTML = ".html";
	public static final String VAZIO = "";
	public static final String DOIS_PONSTOS = ":";
	
	public static final String REGEX_INPUT = "([A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]|\\d|\"|\'|:|,|:|@|#|\\$|%|\\(|\\)|\\[|\\]|\\{|\\}|\\||\\.| |=|\\/|\n|\\u00A0|!|>)*";
	public static final String REGEX_MAX_LENGTH = "maxlength.*\"(.*)\"";
	public static final String REGEX_LABEL = "value.*\"(.*)\"";
	public static final String REGEX_PRECISION = "precision(.*)}";
	public static final String REGEX_NG_MODEL = "\\[\\(ngModel\\)\\].*\"(.*)\"";

}
