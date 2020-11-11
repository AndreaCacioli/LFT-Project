import java.io.*;

public class Parser {
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
	     }
       else error("syntax error");
    }

    public void start() //S
    {
    	switch(look.tag)
      {
        case '(':
        case NumberTok.tag:
        case Tag.EOF:
          expr();
          match(Tag.EOF);
          break;
        default:
          error("your expression should start with either a number or a parenthesis");
          break;
      }
    }

    private void expr() //A
    {
      switch(look.tag)
      {
        case NumberTok.tag:
        case '(':
          term();
          exprp();
          break;
        default:
          error("syntax error in expr");
          break;
      }
    }

    private void exprp() //B
    {
      switch (look.tag)
      {
        case '+':
          match(Token.plus.tag);
          term();
          exprp();
          break;
        case '-':
          match(Token.minus.tag);
          term();
          exprp();
          break;
        case ')':
          break;

      }
    }

    private void term() //C
    {
      switch (look.tag)
      {
        case NumberTok.tag:
        case '(':
          fact();
          termp();
          break;
        default:
          error("syntax error in term");
          break;
      }
    }

    private void termp() //D
    {
      switch (look.tag)
      {
        case '(':
        case ')':
        case NumberTok.tag:
          break;
        case '*':
          match(Token.mult.tag);
          termp();
          fact();
          break;
        case '/':
          match(Token.div.tag);
          termp();
          fact();
          break;
      
      }
    }

    private void fact() //E
    {
      switch (look.tag)
      {
        case '(':
          match(Token.lpt.tag);
          expr();
          match(Token.rpt.tag);
          break;
        case NumberTok.tag:
          match(NumberTok.tag);
          break;
        default:
          error("syntax error in fact");
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
            parser.start();
            System.out.println("Input OK");
            br.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }
}
