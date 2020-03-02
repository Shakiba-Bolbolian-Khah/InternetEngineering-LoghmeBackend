package Repository;

public enum OrderState{
    Searching, Delivering, Done;

    public String getStateAsString(OrderState state) {
        switch (state){
            case Searching:
                return "Searching";
            case Delivering:
                return "Delivering";
            case Done:
                return "Done";
            default:
                return "";
        }
    }
}