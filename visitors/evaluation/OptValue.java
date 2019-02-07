package visitors.evaluation;

public class OptValue implements Value {
	protected final Value value;
	private boolean Empty=false;
	public OptValue(Value value) {
        this.value = value;
        
    }

    public OptValue(OptValue op) {
        this.value = op.getValue();

    }

 
    @Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof OptValue))
			return false;
		return (this.Empty==true && ((OptValue)obj).Empty==true)||(value.equals(((OptValue) obj).value) &&(this.Empty==false && ((OptValue)obj).Empty==false) );
	}

    @Override
    public int hashCode() {
        return 31* value.hashCode();
    }

    @Override
    public OptValue asOpt() {
        return this;
    }

    @Override
    public String toString() {
        return "opt " + ((Empty)? "empty" : value.toString());
    }
	@Override
	public boolean isEmpty(){
		return Empty;
		
	}
	@Override
	public void modifyEmpty() {
		Empty=true;
	}
	public Value getValue() {
        return value;
	}

}
