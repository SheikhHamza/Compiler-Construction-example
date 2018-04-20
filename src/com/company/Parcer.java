package com.company;

import jdk.nashorn.internal.ir.Assignment;

import java.util.ArrayList;

import static javafx.application.Platform.exit;

public class Parcer
{
	String look = new String();
	ArrayList<TokenLex> tokens_and_lexiam = null;
	int currentToken = -1;

	public void setTokens_and_lexiam(ArrayList<TokenLex> tokens_and_lexiam)
	{
		this.tokens_and_lexiam = tokens_and_lexiam;
	}

	void start(){
		look = nextToken();
		FuncDec();
	}
	void FuncDec()
	{
		RetType();
		ID();
		if( look.equals("("))
		{
			match("(");
			Params();
			if(look.equals(")"))
			{
				match(")");
				if(look.equals("{"))
				{
					match("{");
					Statements();
					if(look.equals("}"))
					{
						match("}");
						FuncDec();
					}
					else
					{
						ParserError("Closing bracket missing");
					}
				}
				else {
					ParserError("Opening bracket missing");
				}
			}
			else
			{
				ParserError("Closing bracket missing");
			}
		}
		else
		{
			ParserError("Opening bracket missing");
		}
	}
	void RetType()
	{
		if(look.equals("INT"))
		{
			match("INT");
		}
		else if (look.equals("CHAR"))
		{
			match("CHAR");
		}
		else if (look.equals("VOID"))
		{
			match("VOID");
		}
		else {
			ParserError("Undefined return type");
		}

	}
	void DataType(){
		if(look.equals("INT"))
		{
			match("INT");
			if(look.equals("["))
			{
				match("[");
				if(look.equals("]"))
				{
					match("]");
				}
				else
				{
					ParserError("Clossing bracket missing");
				}
			}
		}
		else if (look.equals("CHAR"))
		{
			match("CHAR");
			match("[");
			if(look.equals("]"))
			{
				match("]");
			}
			else
			{
				ParserError("Clossing bracket missing");
			}
		}
		else {
			ParserError("Undefined data type");
		}
	}
	void Params()
	{
		DataType();
		ID();
		MultiParams();
	}
	void MultiParams()
	{
		if (look.equals(","))
		{
			match(",");
			DataType();
			ID();
			MultiParams();
		}
	}
	void Statements(){
		if(look.equals("IF") || look.equals("WHILE") || look.equals("INT") || look.equals("CHAR") || look.equals("ID"))
		{
			Statement();
			Statements();
		}
	}
	void Statement()
	{
		if( look.equals("IF"))
		{
			IF();
		}
		else if (look.equals("WHILE")){
			WHILE();
		}
		else if(look.equals("INT") || look.equals("CHAR"))
		{
			VariableDec();
		}
		else if (look.equals("ID"))
		{
			match("ID");
			if(look.equals("["))
			{
				match("[");
				if(look.equals("ID"))
				{
					match("ID");
				}
				else if (look.equals("NUM"))
				{
					match("NUM");
				}
				else
				{
					ParserError("Unexpected Character");
				}
				if(look.equals("]"))
				{
					match("]");
				}
				else
				{
					ParserError("Closing Bracket Missing");
				}
			}
			statement2();
		}
	}
	void statement2(){
		if(look.equals("ID") || look.equals("NUM"))
		{
			InputParams();
		}
		if (look.equals("="))
		{
			match("=");
			VariableAssign2();

		}
	}
	void InputParams(){
		if(look.equals("ID"))
		{
			match("ID");
			MultiInpurParams();
		}
		else if(look.equals("NUM"))
		{
			match("NUM");
			MultiInputParams2();
		}
	}
	void MultiInpurParams(){
		if(look.equals(","))
		{
			match(",");
			MultiInputParams2();
		}
	}
	void MultiInputParams2(){
		if(look.equals("ID"))
		{
			match("ID");
			MultiInpurParams();
		}
		else if(look.equals("NUM"))
		{
			match("NUM");
			MultiInpurParams();
		}
		else
		{
			ParserError("Unexpected character");
		}
	}
	void VariableAssign()
	{
		if(look.equals("ID"))
		{
			match("ID");
			if(look.equals("="))
			{
				match("=");
				VariableAssign2();
			}
		}
		else
		{
			ParserError("Unexpected character");
		}
	}
	void VariableAssign2()
	{
		if(look.equals("ID") && tokens_and_lexiam.get(currentToken+1).getToken().equals("("))
		{
			match("ID");
			FunctionCAll();
			if(look.equals(";"))
			{
				match(";");
			}
			else
			{
				ParserError("Semicolon mising");
			}
		}
		else
		{
			Exp();
			if(look.equals(";"))
			{
				match(";");
			}
			else
			{
				ParserError("Semicolon mising");
			}
		}
	}
	void IF()
	{
		match("IF");
		if( look.equals("("))
		{
			match("(");
			BoolExp();
			if(look.equals(")"))
			{
				match(")");
				if(look.equals("{"))
				{
					match("{");
					Statements();
					if(look.equals("}"))
					{
						match("}");
						Else();
					}
					else
					{
						ParserError("Closing bracket missing");
					}
				}
				else
				{
					ParserError("Opening bracket missing");
				}
			}
			else
			{
				ParserError("Closing bracket missing");
			}
		}
		else
		{
			ParserError("Opening bracket missing");
		}
	}
	void Else()
	{
		if(look.equals("ELSE"))
		{
			match("ELSE");
			if(look.equals("{"))
			{
				match("{");
				Statements();
				if(look.equals("}"))
				{
					match("}");
				}
				else {
					ParserError("Closing bracket missing");
				}
			}
			else
			{
				ParserError("Opening bracket missing");
			}
		}

	}
	void BoolExp()
	{
		Exp();
		RelExp();
	}
	void WHILE()
	{
		if(look.equals("WHILE"))
		{
			match("WHILE");
			if(look.equals("("))
			{
				match("(");
				BoolExp();
				if(look.equals(")"))
				{
					match(")");
					if(look.equals("{"))
					{
						match("{");
						Statements();
						if(look.equals("}"))
						{
							match("}");
						}
						else {
							ParserError("Closing bracket missing");
						}
					}
					else {
						ParserError("Opening bracket missing");
					}
				}
				else {
					ParserError("Closing bracket missing");
				}
			}
			else {
				ParserError("Opening bracket missing");
			}
		}
		else
		{
			ParserError("invalid token");
		}
	}
	void RelExp()
	{
		if(look.equals("RO"))
		{
			match("RO");
			Exp();
		}
		else
		{
			ParserError("Invalid token");
		}
	}
	void Exp()
	{
		T();
		E2();
	}
	void E2(){
		if(look.equals("+"))
		{
			match("+");
			T();
			E2();
		}
		else if(look.equals("-"))
		{
			match("-");
			T();
			E2();
		}
	}
	void T()
	{
		F();
		T2();
	}
	void F()
	{
		if(look.equals("ID"))
		{
			match("ID");
			if(look.equals("["))
			{
				match("[");
				if(look.equals("ID"))
				{
					match("ID");
				}
				else if (look.equals("NUM"))
				{
					match("NUM");
				}
				else
				{
					ParserError("Unexpected Character");
				}
				if(look.equals("]"))
				{
					match("]");
				}
				else
				{
					ParserError("Closing Bracket Missing");
				}
			}
		}
		else if(look.equals("NUM"))
		{
			match("NUM");
		}
		else if(look.equals("("))
		{
			match("(");
			Exp();
			if(look.equals(")"))
			{
				match(")");
			}
			else
			{
				ParserError("Closing bracket missing");
			}
		}

	}
	void T2()
	{
		if(look.equals("*"))
		{
			match("*");
			F();
			T2();
		}
		else if(look.equals("/"))
		{
			match("/");
			F();
			T2();
		}
	}
	void VariableDec()
	{
		DataType();
		ID();
		while (look.equals(","))
		{
			match(",");
			if(look.equals("ID"))
			{
				ID();
			}
		}
		if(look.equals(";"))
		{
			match(";");
		}
		else {
			ParserError("Semicolon missing");
		}
	}
	void Assignment()
	{
		ID();
		if(look.equals("="))
		{
			match("=");
			Value();
			if(look.equals(";"))
			{
				match(";");
			}
			else
			{
				ParserError("Semicolon mising");
			}
		}
		else {
			ParserError("Syntax error");
		}
	}
	void Value(){
		FunctionCAll();
	}
	void FunctionCAll(){
		ID();
		if(look.equals("("))
		{
			match("(");
			//InputParams();
			if(look.equals(")"))
			{
				match(")");
			}
			else
			{
				ParserError("Closing bracket missing");
			}
		}
		else
		{
			ParserError("Opening brackets missing");
		}
	}
	void ID(){
		if(look.equals("ID"))
		{
			match("ID");
		}
	}
	void match(String str){
		if (look.equals(str))
		{
			System.out.print(look);
			look = nextToken();
		}
		else
		{
			System.out.print("Bad Token");
		}
	}
	void ParserError(String str){

	}
	String nextToken()
	{
		if(currentToken == (tokens_and_lexiam.size()-1))
		{
			System.exit(0);
		}
		currentToken++;
		return tokens_and_lexiam.get(currentToken).getToken();
	}
}
