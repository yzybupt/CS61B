import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture; // dependent
    private double[][] energyMatrix; // dependent

    public SeamCarver(Picture picture) {
        this.picture = picture;
        setEnergyMatrix();
    }


    public Picture picture() { // current picture
        return this.picture;
    }


    public int width() {  // width of current picture
        return picture.width();
    }


    public int height() {  // height of current picture
        return picture.height();
    }


    public double energy(int x, int y) { // energy of pixel at column x and row y
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new java.lang.IndexOutOfBoundsException("The passed in argument is not within border");
        }

        int rgbleft = picture.getRGB(xIndexRounding(x - 1), yIndexRounding(y));
        int r1 = (rgbleft >> 16) & 0xFF;
        int g1 = (rgbleft >>  8) & 0xFF;
        int b1 = (rgbleft >>  0) & 0xFF;


        int rgbright = picture.getRGB(xIndexRounding(x + 1), yIndexRounding(y));
        int r2 = (rgbright >> 16) & 0xFF;
        int g2 = (rgbright >>  8) & 0xFF;
        int b2 = (rgbright >>  0) & 0xFF;

        double xEnergy = (r1-r2) * (r1 - r2) + (g1-g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);

        int rgbup = picture.getRGB(xIndexRounding(x), yIndexRounding(y - 1));
        r1 = (rgbup >> 16) & 0xFF;
        g1 = (rgbup >>  8) & 0xFF;
        b1 = (rgbup >>  0) & 0xFF;


        int rgbdown = picture.getRGB(xIndexRounding(x), yIndexRounding(y + 1));
        r2 = (rgbdown >> 16) & 0xFF;
        g2 = (rgbdown >>  8) & 0xFF;
        b2 = (rgbdown >>  0) & 0xFF;

        double yEnergy = (r1-r2) * (r1 - r2) + (g1-g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);

        return xEnergy + yEnergy;
    }

    public int[] findHorizontalSeam() {  // sequence of indices for horizontal seam
        Picture transposePic = new Picture(height(),width());
        transposePic.setOriginUpperLeft();
        int[] colors = new int[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = height() - 1; j >= 0; j--) {
                colors[i * height() + height() - 1 - j] = picture.getRGB(i, j);
            }
        }


        for (int j = 0; j < transposePic.height(); j++) {
            for (int i = 0; i < transposePic.width(); i++) {
                transposePic.setRGB(i, j, colors[j * transposePic.width() + i]);
            }
        }

        SeamCarver transposeSeam = new SeamCarver(transposePic);
        int[] temp = transposeSeam.findVerticalSeam();
        for (int i = 0; i < temp.length; i++) { // 修正一下因为右旋转所带来的坐标扭曲
            temp[i] = transposePic.width() - 1 - temp[i];
        }
        return temp;
    }

    public int[] findVerticalSeam() { // sequence of indices for vertical seam
        double[][] cumulatedMatrix = new double[energyMatrix.length][energyMatrix[0].length];
        for (int i = 0; i < energyMatrix.length; i++) {
            for (int j = 0; j < energyMatrix[0].length; j++) {
                cumulatedMatrix[i][j] = energyMatrix[i][j];
            }
        }

        for (int i = 1; i < energyMatrix.length; i++) { // 第一行不需要处理
            for (int j = 0; j < energyMatrix[0].length; j++) { //
                cumulatedMatrix[i][j] = verticalHelper(cumulatedMatrix, j, i);
            }
        }//此处完成对能量积累矩阵的计算


        int[] path = new int[height()];
        int index = height() - 1;
        int x = 0;
        double min = energyMatrix[height() - 1][0];
        for (int i = 1; i < width(); i++) {
            if (energyMatrix[height() - 1][i] < min) {
                min = energyMatrix[height() - 1][i];
                x = i;
            }
        }
        path[index] = x;
        index--;

        for (int i = energyMatrix.length - 1; i >= 1; i--) { // 第一行不需要处理
                x = verticalHelper1(cumulatedMatrix, x, i);
                path[index] = x;
                index--;
        }

        return path;
    }

    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from picture
        if(seam.length != width() || !arrayLegal(seam)) {
            throw new java.lang.IllegalArgumentException("Passed in seam is not legal");
        }
        Picture seamedPic = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            int k = 0;
            for (int j = 0 ; j < height(); j++) {
                if (j != seam[i]) {
                    seamedPic.setRGB(i, k, picture.getRGB(i, j));
                    k++;
                }
            }
        }
        this.picture = seamedPic;
        setEnergyMatrix();
    }

    public void removeVerticalSeam(int[] seam) { // remove vertical seam from picture
        if(seam.length != height() || !arrayLegal(seam)) {
            throw new java.lang.IllegalArgumentException("Passed in seam is not legal");
        }
        Picture seamedPic = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            int k = 0;
            for (int i = 0 ; i < width(); i++) {
                if (i != seam[j]) {
                    seamedPic.setRGB(k, j, picture.getRGB(i, j));
                    k++;
                }
            }
        }
        this.picture = seamedPic;
        setEnergyMatrix();
    }

    private int xIndexRounding(int x) {
        if (x >= 0 && x <= width() - 1) {
            return x;
        } else if (x < 0) {
            return width() + (x % width());
        } else {
            return x % width();
        }
    }

    private int yIndexRounding(int y) {
        if (y >= 0 && y <= height() - 1) {
            return y;
        } else if (y < 0) {
            return height() + (y % height());
        } else {
            return y % height();
        }
    }

    private void setEnergyMatrix() {
        double[][] matrix = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                matrix[i][j] = energy(j, i);
            }
        }
        energyMatrix = matrix;
    }

    private double verticalHelper(double[][] matrix, int x, int y) {
        if( x > 0 && x < width() - 1) {
            double t1 = matrix[y - 1][x - 1];
            double t2 = matrix[y - 1][x];
            double t3 = matrix[y - 1][x + 1];
            return Math.min(Math.min(t1, t2), t3);
        } else if (x == 0) {
            double t1 = matrix[y - 1][x];
            double t2 = matrix[y - 1][x + 1];
            return Math.min(t1, t2);
        } else {
            double t1 = matrix[y - 1][x - 1];
            double t2 = matrix[y - 1][x];
            return Math.min(t1, t2);
        }
    }

    private int verticalHelper1(double[][] matrix, int x, int y) {
        if( x > 0 && x < width() - 1) {
            double t1 = matrix[y - 1][x - 1];
            double t2 = matrix[y - 1][x];
            double t3 = matrix[y - 1][x + 1];
            double min = Math.min(Math.min(t1, t2), t3);
            if (min == t1) {
                return x - 1;
            }
            if (min == t2) {
                return x;
            }
            return x + 1;
        } else if (x == 0) {
            double t1 = matrix[y - 1][x];
            double t2 = matrix[y - 1][x + 1];
            double min = Math.min(t1, t2);
            if (min == t1) {
                return x;
            }
            return x + 1;
        } else {
            double t1 = matrix[y - 1][x - 1];
            double t2 = matrix[y - 1][x];
            double min = Math.min(t1, t2);
            if (min == t1) {
                return x - 1;
            }
            return x;

        }
    }

    private boolean arrayLegal(int[] path) { // 检查是否连续的两个数之间相差大于1
        if (path == null) {
            return false;
        }
        for (int i = 0; i < path.length - 1; i++) {
            if (Math.abs(path[i] - path[i + 1]) > 1) {
                return false;
            }
        }
        return true;

    }


}
