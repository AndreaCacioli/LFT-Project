import java.io.*;

public class Parser
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br)
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
    	} else error("syntax error");
    }

    public void prog()//S
    {
      switch(look.tag)
      {
        case 257:
        case Tag.READ:
        case Tag.WHILE:
        case Tag.PRINT:
        case Tag.CASE:
        case '{':
        statlist();
        break;
        default:
        error("S");
       }
    	match(Tag.EOF);
    }

    private void statlist()//A
    {
      switch(look.tag)
      {
        case 257:
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

      }

    }

    private void stat()//C
    {
      switch(look.tag)
      {
        case 257:
        match(257);
        expr();
        break;
        case Tag.READ:
        match(Tag.READ);
        match('(');
        match(257);
        match(')');
        break;
        case Tag.WHILE:
        match(Tag.WHILE);
        match('(');
        bexpr();
        match(')');
        stat();
        break;
        case Tag.PRINT:
        match(Tag.PRINT);
        match('(');
        exprlist();
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
    }
    private void whenitem()//F
    {
      if(look.tag == Tag.WHEN)
      {
        match(Tag.WHEN);
        match('(');
        bexpr();
        match(')');
        match(Tag.DO);
        stat();

      }
      else
      {
        error("F");
      }
    }
    private void bexpr()//G
    {
      if(look.tag == Tag.RELOP)
      {
        match(Tag.RELOP);
        expr();
        expr();
      }
      else
      {
        error("G");
      }
    }
    private void expr()//H
    {
      switch(look.tag)
      {
        case '+':
        match('+');
        match('(');
        exprlist();
        match(')');
        break;
        case '*':
        match('*');
        match('(');
        exprlist();
        match(')');
        break;
        case '-':
        match('-');
        expr();
        expr();
        break;
        case '/':
        match('/');
        expr();
        expr();
        break;
        case 257:
        match(257);
        break;
        case 256:
        match(NumberTok.tag);
        break;
        default:
        error("H");
        break;
      }
    }
    private void exprlist()//I
    {
      switch(look.tag)
      {
        case '+':
        case '-':
        case '*':
        case '/':
        case 257:
        case NumberTok.tag:
        expr();
        exprlistp();
        default:
        error("I");
        break;
      }
    }
    private void exprlistp()//J
    {
      switch(look.tag)
      {
        case '+':
        case '-':
        case '*':
        case '/':
        case 257:
        case NumberTok.tag:
        expr();
        exprlistp();
        break;
        case ')':
        break;
      }
    }


    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "../Input.txt"; // il percorso del file da leggere
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
