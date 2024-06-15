package com.fiends.moneyview.tools;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Tuple extends ArrayList<String> {

	public Tuple( String ... values ) {
		super(Arrays.asList(values));
	}

	public String toString(String join){
		return StringUtils.join( this, join );
	}

	public String toTabString(){
		return StringUtils.join( this, "\t" );
	}

	public boolean append(Object what){
		if (what==null) return add( null );
		return add(what.toString());
	}
}
