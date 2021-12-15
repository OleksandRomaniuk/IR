package UsingCollections;

public class IllegalQueryException extends Exception{
    String message;
    IllegalQueryException(){
        super();
    }

    IllegalQueryException(String mes){
        super(mes);
        message = "Illegal Query: " + mes + "!!!";
    }

    @Override
    public void printStackTrace() {
        System.out.print(message);
    }
}
