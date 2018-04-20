package com.company;

public class TokenLex
{
	private String token = new String();
	private String lexiam = new String();

	public TokenLex(String token, String lexiam)
	{
		this.token = token;
		this.lexiam = lexiam;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getLexiam()
	{
		return lexiam;
	}

	public void setLexiam(String lexiam)
	{
		this.lexiam = lexiam;
	}
}
