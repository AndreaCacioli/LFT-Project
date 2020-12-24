import java.io.*;

public class Translator
{

  /*
      Characters: o: output -> print
                  s: sum
                  p: product
                ' ': character not needed
                  m: minus
                  d: division
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
        statlist(/*lnext_prog*/);
        //code.emitLabel(lnext_prog); is this needed???
        match(Tag.EOF);
        break;

        default:
          error("BIG OOF TIME");
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

        private void statlist(/*int lnext_prog*/)//A
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
            error("A");
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
              error("B");
          }

        }

    public void stat( /* completare */ ) //C
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
              expr(' ');
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
              exprlist('o'); //this exprlist should be written
              match(')');
              break;
            case Tag.CASE:
            match(Tag.CASE);
            whenlist();
            match(Tag.ELSE);
            stat();
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

     private void whenlist()//D
     {
       if(look.tag == Tag.WHEN)
       {
         whenitem();
         whenlistp();
       }
       else
       {
         error("D");
       }
     }

     private void whenlistp()//E
     {
       if(look.tag == Tag.WHEN)
       {
         whenitem();
         whenlistp();
       }
       else if(look.tag == Tag.ELSE)
       {
         //eps
       }
       else
       {
         error("E");
       }
     }
     private void whenitem()//F
     {
       if(look.tag == Tag.WHEN)
       {
         match(Tag.WHEN);
         match('(');
         bexpr(12345678); //TODO USE LABEL
         match(')');
         match(Tag.DO);
         stat();

       }
       else
       {
         error("F");
       }
     }
     private void bexpr(int jumpLabel)//G
     {
       if(look.tag == Tag.RELOP)
       {
         String operator = ((Word)look).lexeme;
         match(Tag.RELOP);
         expr(' ');
         expr(' ');
         switch (operator)
         {
           case "==":
              code.emit(OpCode.if_icmpne, jumpLabel);
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
            error("Not a valid relational operator!");
         }
       }
       else
       {
         error("G");
       }
     }

    private void expr( char operation)//H
    {
        switch(look.tag)
        {
          case '+':
            match('+');
            match('(');
            exprlist('s');
            match(')');
            if(operation == 'o') code.emit(OpCode.invokestatic,1);
            break;

          case '*':
            match('*');
            match('(');
            exprlist('p');
            match(')');
            if(operation == 'o') code.emit(OpCode.invokestatic,1);
            break;

            case '-':
                match('-');
                expr('m');
                expr('m');
                code.emit(OpCode.isub);
                if(operation == 'o') code.emit(OpCode.invokestatic,1);
                break;
            case '/':
                  match('/');
                  expr('d');
                  expr('d');
                  code.emit(OpCode.idiv);
                  if(operation == 'o') code.emit(OpCode.invokestatic,1);
                break;

            case Tag.ID: //Identificatore

                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1)
                {
                   error("Error: variable " + Tag.ID + " not defined in this context!");
                }

                code.emit(OpCode.iload, id_addr);
                if (operation == 'o') code.emit(OpCode.invokestatic,1);
                match(Tag.ID);
                break;
            case NumberTok.tag:
                code.emit(OpCode.ldc, ((NumberTok)look).lexeme);
                match(NumberTok.tag);
                if(operation == 'o') code.emit(OpCode.invokestatic,1);
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
          expr(operation);
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
          expr(operation);
          exprlistp(operation);
          if (operation == 'p') code.emit(OpCode.imul);
          if (operation == 's') code.emit(OpCode.iadd);
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
