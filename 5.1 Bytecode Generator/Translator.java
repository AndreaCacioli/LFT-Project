import java.io.*;

public class Translator
{

  /*
      Characters: o: output -> print
                  s: sum
                  p: product
  */

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0; //Tiene il numero di variabili nel programma

    public Translator(Lexer l, BufferedReader br)
    {
        lex = l;
        pbr = br;
        move();
    }

    void move()
    {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s)
    {
	     throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t)
    {
	     if (look.tag == t)
       {
	        if (look.tag != Tag.EOF) move();
	     }
       else error("syntax error");
    }


    public void prog()//S
    {
      switch(look.tag)
      {
        case '=':
        case Tag.READ:
        case Tag.WHILE:
        case Tag.PRINT:
        case Tag.CASE:
        case '{':
        int lnext_prog = code.newLabel();
        statlist();
        match(Tag.EOF);
        break;

        default:
          error("A program should start with one of the following:\nread\nprint\nwhile\ncond\nAn open curly brace!");
        break;
      }
        try
        {
        	code.toJasmin();
        }
        catch(java.io.IOException e)
        {
        	System.out.println("IO error\n");
        }

    }

        private void statlist()//A
        {
          switch(look.tag)
          {
            case '=':
            case Tag.READ:
            case Tag.WHILE:
            case Tag.PRINT:
            case Tag.CASE:
            case '{':
            stat();
            statlistp();
            break;
            default:
            error("A program should contain one of the following:\nread\nprint\nwhile\ncond\nAn open curly brace!");
          }
        }

        private void statlistp()//B
        {
          switch(look.tag)
          {
            case ';':
              match(';');
              stat();
              statlistp();
              break;
            case '}':
    		    case Tag.EOF:
                break;
            default:
              error("Your list of instructions has to be separated by a semicolumn or wrapped by curly braces.");
          }

        }

    public void stat(  ) //C
    {
        switch(look.tag)
        {
            case '=':
              match('=');
              int found = st.lookupAddress(((Word)look).lexeme);
              if(found == -1)
              {
                found = count;
                st.insert(((Word)look).lexeme,count++);
              }
              match(Tag.ID);
              expr();
              code.emit(OpCode.istore, found);
            break;
            case Tag.WHILE:
              match(Tag.WHILE);
              match('(');
              int startWhileLabel = code.newLabel();
              code.emitLabel(startWhileLabel);
              int endWhileLabel = code.newLabel();
                bexpr(endWhileLabel);
              match(')');
                stat();
                code.emit(OpCode.GOto, startWhileLabel);
                code.emitLabel(endWhileLabel);

              break;
            case Tag.PRINT:
              match(Tag.PRINT);
              match('(');
              exprlist('o');
              match(')');
              break;
            case Tag.CASE:
            int endOfCond = code.newLabel();
              match(Tag.CASE);
              whenlist(endOfCond);
              match(Tag.ELSE);
              stat();
              code.emitLabel(endOfCond);
              break;
            case '{':
            match('{');
            statlist();
            match('}');
            break;


            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID)
                {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1)
                    {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,id_addr);
                }
                else
                    error("Error in grammar (stat) after read( with " + look);
                break;
            default:
            error("C");
            break;
        }
     }

     private void whenlist(int endOfCond)//D
     {
       if(look.tag == Tag.WHEN)
       {
         whenitem(endOfCond);
         whenlistp(endOfCond);
       }
       else
       {
         error("Your conditional statements must start with the keyword 'when' .");
       }
     }

     private void whenlistp(int endOfCond)//E
     {
       if(look.tag == Tag.WHEN)
       {
         whenitem(endOfCond);
         whenlistp(endOfCond);
       }
       else if(look.tag == Tag.ELSE)
       {
         //eps
       }
       else
       {
         error("You must separate each conditional statement with the keyword 'when' and close the 'cond' with keyword 'else'.");
       }
     }
     private void whenitem(int endOfCondLabel)//F
     {
       if(look.tag == Tag.WHEN)
       {
         match(Tag.WHEN);
         match('(');
         int jumpTo = code.newLabel();
         bexpr(jumpTo);
         match(')');
         match(Tag.DO);
         stat();
         code.emit(OpCode.GOto, endOfCondLabel);
         code.emitLabel(jumpTo);

       }
       else
       {
         error("Your conditional statements must start with the keyword 'when'.");
       }
     }
     private void bexpr(int jumpLabel)//G
     {
       if(look.tag == Tag.RELOP)
       {
         String operator = ((Word)look).lexeme;
         match(Tag.RELOP);
         expr();
         expr();
         switch (operator)
         {
           case "==":
              code.emit(OpCode.if_icmpne, jumpLabel);
              break;
          case "<>":
             code.emit(OpCode.if_icmpeq, jumpLabel);
             break;
           case "<=":
             code.emit(OpCode.if_icmpgt, jumpLabel);
             break;
           case "<":
             code.emit(OpCode.if_icmpge, jumpLabel);
             break;
           case ">=":
             code.emit(OpCode.if_icmplt, jumpLabel);
             break;
           case ">":
             code.emit(OpCode.if_icmple, jumpLabel);
             break;
          default:
            error("You did not use a valid relational operator in your conditional statement.");
            break;
         }
       }
       else
       {
         error("A conditional statement must contain a relational operator.");
       }
     }

    private void expr()//H
    {
        switch(look.tag)
        {
          case '+':
            match('+');
            match('(');
            exprlist('s');
            match(')');
            break;

          case '*':
            match('*');
            match('(');
            exprlist('p');
            match(')');
            break;

            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '/':
                  match('/');
                  expr();
                  expr();
                  code.emit(OpCode.idiv);
                  break;

            case Tag.ID: //Identificatore

                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1)
                {
                   error("Error: variable " + Tag.ID + " not defined in this context!");
                }

                code.emit(OpCode.iload, id_addr);
                match(Tag.ID);
                break;
            case NumberTok.tag:
                code.emit(OpCode.ldc, ((NumberTok)look).lexeme);
                match(NumberTok.tag);
                break;
            default:
                error("H");
                break;
        }
    }
    private void exprlist(char operation)//I
    {
      switch(look.tag)
      {
        case '+':
        case '-':
        case '*':
        case '/':
        case Tag.ID:
        case NumberTok.tag:
          expr();
          if (operation == 'o') code.emit(OpCode.invokestatic,1); //Had to put this here, otherwise first element of the list we have to print doesn't get printed
          exprlistp(operation);
          break;
        default:
          error("I");
          break;
      }
    }
    private void exprlistp(char operation)//J
    {
      switch(look.tag)
      {
        case '+':
        case '-':
        case '*':
        case '/':
        case Tag.ID:
        case NumberTok.tag:
          expr();
          if (operation == 'p') code.emit(OpCode.imul);
          if (operation == 's') code.emit(OpCode.iadd);
          if (operation == 'o') code.emit(OpCode.invokestatic,1);
          exprlistp(operation);
          break;
        case ')':
          break;
        default:
          error("J");
      }
    }
    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "../Input.txt"; // il percorso del file da leggere
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK, Generation of code succeded!");
            br.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }
}
