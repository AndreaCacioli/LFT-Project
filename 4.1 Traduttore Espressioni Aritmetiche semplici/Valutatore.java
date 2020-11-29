import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br)
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

    public void start()
    {
	    int expr_val = 0;

    	switch(look.tag)
      {
        case '(':
        case NumberTok.tag:
          expr_val = expr();
          match(Tag.EOF);
          System.out.println(expr_val);
          break;
        default:
          error("your expression should either start with a number or a parenthesis");
          break;
      }
    }

    private int expr()
    {
	    int term_val = 0, exprp_val = 0;

      switch(look.tag)
      {
        case NumberTok.tag:
        case '(':
          term_val = term();
          exprp_val = exprp(term_val); //{exprp.i=term.val}
          break;
        default:
          error("syntax error in expr");
          break;
      }
	    return exprp_val; //{expr.val=exprp.val}
    }

    private int exprp(int exprp_i)
    {
	    int term_val = 0, exprp_val = 0;
	    switch (look.tag)
      {
  	    case '+':
              match('+');
              term_val = term();
              exprp_val = exprp(exprp_i + term_val); //{exprp1.i=exprp.i+term.val}
              break;
        case '-':
              match(Token.minus.tag);
              term_val = term();
              exprp_val = exprp(exprp_i - term_val); //{exprp1.i=exprp.i−term.val}
              break; //{exprp.val=exprp1.val}
        case ')':
        case Tag.EOF:
              exprp_val = exprp_i;
              break; //{exprp.val=exprp.i}
        default:
              error("syntax error in exprp");
              break;
	    }
      return exprp_val;
    }

    private int term()
    {
      int fact_val = 0, termp_val = 0;
      switch (look.tag)
      {
        case NumberTok.tag:
        case '(':
          fact_val = fact();
          termp_val = termp(fact_val);
          break;
        default:
          error("syntax error in term");
          break;
      }
      return termp_val;
    }

    private int termp(int termp_i)
    {
      int fact_val = 0, termp_val = 0;
      switch (look.tag)
      {
        case ')':
        case Tag.EOF:
        case '+':
        case '-':
          termp_val = termp_i;
          break;
        case '*':
          match(Token.mult.tag);
          fact_val = fact();
          termp_val = termp(fact_val * termp_i);
          break;
        case '/':
          match(Token.div.tag);
          fact_val = fact();
          termp_val = termp(termp_i / fact_val);
          //System.out.println("termp: Returning " + termp_val + " to the calling method as fact_val=" + fact_val + " and termp_i=" + termp_i + " and I have to divide them!");
          break;
        default:
          error("syntax error in termp");
          break;
      }

      return termp_val;
    }

    private int fact()
    {
      int fact_val = 0;
      switch (look.tag)
      {
        case '(':
          match(Token.lpt.tag);
          fact_val = expr();
          match(Token.rpt.tag);
          break;
        case NumberTok.tag:
          fact_val = (int)((NumberTok)look).lexeme; //We need to get the value of the token before we check, otherwise we try to cast a token into a NumberTok
          match(NumberTok.tag);
          break;
        default:
          error("syntax error in fact");
          break;
      }
      //System.out.println("fact: Returning " + fact_val + " to the calling method");
      return fact_val;
    }

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "../Input.txt";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
