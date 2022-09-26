public class Cell{
    private int player;
    boolean isEmpty;

    public Cell(){
        player = -1;
        isEmpty = true;
    }
    public int getPlayer(){
        return player;
    }
    public void setPlayer(int p){
        if (p < 0) {
            return;
        }
        player = p;
        isEmpty = false;
    }
}
