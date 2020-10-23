import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

/*
input: format: rows colums   . -> free cell # -> blocked cell 
7 11
...........
.#########.
...#...#...
S#.#.#.#.#T
.#...#...#.
.#########.
...........
 */
/*
4 4
..S#
..#T
.#..
#...
 */
/*
4 4
..S#
...T
.#..
#...
 */
public class HRMaze {
    static class Cell {
        int row,col,cost;
        Cell parent;
        public Cell(int row, int col, int cost,Cell parent) {
            this.row = row;
            this.col = col;
            this.cost = cost;
            this.parent = parent;
        }
    }

    static JFrame frame;
    static JButton b[][];

    static int n,m;
    static int[][] grid,dist;
    static int sr,sc;
    static int dr,dc;
    static List<int[]> res;

    public static void view(List<int[]> res) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        b[sr][sc].setForeground(Color.RED);
        b[dr][dc].setForeground(Color.RED);

        for(int i = 1;i<res.size()-1;i++) {
            int[] ar = res.get(i);
            b[ar[0]][ar[1]].setBackground(Color.green);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.dispose();
    }

    public static void main(String[] args) throws IOException {

        res = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        frame = new JFrame("path");
        frame.setSize(500,450);
        frame.setLayout(new GridLayout(n,m));
        b = new JButton[n][m];

        grid = new int[n][m];
        dist = new int[n][m];
        for(int[] d:dist) {
            Arrays.fill(d,Integer.MAX_VALUE);
        }

        for(int i = 0;i<n;i++) {
            String str = br.readLine();
            for(int j = 0;j<m;j++) {
                char ch = str.charAt(j);
                if(ch=='#') {
                    grid[i][j] = 0;
                    b[i][j] = new JButton();
                    b[i][j].setBackground(Color.BLACK);
                    frame.add(b[i][j]);
                }
                else if(ch=='.') {
                    grid[i][j] = 1;
                    b[i][j] = new JButton();
                    b[i][j].setBackground(Color.WHITE);
                    frame.add(b[i][j]);
                }
                else if(ch=='S') {
                    grid[i][j] = 1;
                    sr = i;
                    sc = j;
                    b[i][j] = new JButton("S");
                    b[i][j].setBackground(Color.YELLOW);
                    frame.add(b[i][j]);
                }
                else if(ch=='T') {
                    grid[i][j] = 1;
                    dr = i;
                    dc = j;
                    b[i][j] = new JButton("D");
                    b[i][j].setBackground(Color.YELLOW);
                    frame.add(b[i][j]);
                }
            }
        }
        Cell cell = solve(sr,sc,dr,dc);
        System.out.println(printpathcell(cell)-1);
        frame.setVisible(true);
        view(res);
    }

    private static Cell solve(int sr, int sc, int dr, int dc) {
        int[][] dirs = {
                {0,1},
                {1,0},
                {-1,0},
                {0,-1}
        };
        dist[sr][sc] = 0;
        Cell src = new Cell(sr,sc,dist[sr][sc],null);

        Set<String> visited = new HashSet<>();
        String key = sr+""+sc;
        visited.add(key);

        Queue<Cell> q = new LinkedList<>();
        q.add(src);

        while(!q.isEmpty()) {
            int size = q.size();
//            System.out.println("size: "+size);
            for(int i = 0;i<size;i++) {
                Cell curr = q.poll();
                int row = curr.row;
                int col = curr.col;
                int cost = curr.cost;
//                System.out.println("("+row+","+col+") => "+cost);
                if(row==dr && col==dc) {
//                    System.out.println("found path");
                    return curr;
                }
                for(int[] dir : dirs) {
                    int r = row + dir[0];
                    int c = col + dir[1];

                    if(!isValid(r,c)) continue;
                    key = r+""+c;

                    if(visited.contains(key)) continue;
                    visited.add(key);

                    if(dist[r][c]>cost+grid[r][c]) {
//                        System.out.println(dist[r][c]+" > "+cost+" + "+grid[r][c]);
                        dist[r][c] = cost + grid[r][c];
                        Cell next = new Cell(r,c,dist[r][c],curr);
                        q.add(next);
                    }
                }
            }
//            System.out.println("=========");
        }
//        System.out.println("cost from ("+sr+","+sc+") to ("+dr+","+dc+") : "+dist[dr][dc]);
        return null;
    }

    private static boolean isValid(int r, int c) {
        if(r>=0 && r<n && c>=0 && c<m && grid[r][c]!=0) {
            return true;
        }
        return false;
    }

    private static int printpathcell(Cell cell) {
        if(cell==null) {
            return 0;
        }
        int len = printpathcell(cell.parent);
        res.add(new int[]{cell.row,cell.col});
        return len+1;
    }

    private static void printarr(int[][] grid) {
        for(int[] ar : grid) {
            System.out.println(Arrays.toString(ar));
        }
    }
}
