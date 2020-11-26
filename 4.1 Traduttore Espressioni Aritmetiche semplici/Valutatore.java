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
	    int expr_val;

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
	    int term_val, exprp_val;

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
	    int term_val, exprp_val;
	    switch (look.tag)
      {
  	    case '+':
              match('+');
              term_val = term();
              exprp_val = exprp(exprp_i + term_val); //{exprp1.i=exprp.i+term.val}
              return exprp_val;
        case '-':
              match(Token.minus.tag);
              term_val = term();
              exprp_val = exprp(exprp_i - term_val); //{exprp1.i=exprp.i−term.val}
              return exprp_val; //{exprp.val=exprp1.val}
        case ')':
        case Tag.EOF:
              exprp_val = exprp_i;
              return exprp_val; //{exprp.val=exprp.i}
              break;
        default:
              error("syntax error in exprp");
              break;
	    }
    }

    private int term()
    {
	     // ... completare ...
    }

    private int termp(int termp_i)
    {
	     // ... completare ...
    }

    private int fact()
    {
	     // ... completare ...
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "../Input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
