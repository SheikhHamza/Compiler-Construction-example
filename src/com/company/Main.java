package com.company;

import java.util.ArrayList;

public class Main
{
	static Lexiam lex = new Lexiam();

	public static void main(String[] args)
	{

		String fileName = "input.txt";

		lex.readFile(fileName);

		lex.lexAnalyser();

		lex.writeOutputs();

		ArrayList<TokenLex> temp = lex.getToken_and_lexiams();
		for(TokenLex t: temp)
		{
			System.out.println(t.getToken()+ " " + t.getLexiam());
		}
		Parcer parser = new Parcer();
		parser.setTokens_and_lexiam(temp);
		parser.start();

	}

}
