package preco;

public class Input {

	private String maxLength;
	private String maxInteger;
	private String precision;
	private String ngModel;
	private String label;

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getNgModel() {
		return ngModel;
	}

	public void setNgModel(String ngModel) {
		this.ngModel = ngModel;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMaxInteger() {
		if(!this.maxLength.equals(Constantes.VAZIO) && !this.precision.equals(Constantes.VAZIO)) {
			maxInteger = String.valueOf(Integer.parseInt(this.maxLength.trim()) - Integer.parseInt(this.precision.trim()));
		}
		return maxInteger;
	}


}
