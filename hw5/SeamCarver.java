import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture; // dependent
    private double[][] energyMatrix; // dependent

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        setEnergyMatrix();
    }


    public Picture picture() { // current picture
        return new Picture(this.picture);
    }


    public int width() {  // width of current picture
        return picture.width();
    }


    public int height() {  // height of current picture
        return picture.height();
    }


    public double energy(int x, int y) { // energy of pixel at column x and row y
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new java.lang.IndexOutOfBoundsException("The passed in argument wrong");
        }

        Color rgbleft = picture.get(xIndexRounding(x - 1), yIndexRounding(y));
        int r1 = rgbleft.getRed();
        int g1 = rgbleft.getGreen();
        int b1 = rgbleft.getBlue();


        Color rgbright = picture.get(xIndexRounding(x + 1), yIndexRounding(y));
        int r2 = rgbright.getRed();
        int g2 = rgbright.getGreen();
        int b2 = rgbright.getBlue();

        double xEnergy = (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);

        Color rgbup = picture.get(xIndexRounding(x), yIndexRounding(y - 1));
        r1 = rgbup.getRed();
        g1 = rgbup.getGreen();
        b1 = rgbup.getBlue();


        Color rgbdown = picture.get(xIndexRounding(x), yIndexRounding(y + 1));
        r2 = rgbdown.getRed();
        g2 = rgbdown.getGreen();
        b2 = rgbdown.getBlue();

        double yEnergy = (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);

        return xEnergy + yEnergy;
    }

    public int[] findHorizontalSeam() {  // sequence of indices for horizontal seam
        Picture transposePic = new Picture(height(), width());
        transposePic.setOriginUpperLeft();
        Color[] colors = new Color[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = height() - 1; j >= 0; j--) {
                colors[i * height() + height() - 1 - j] = picture.get(i, j);
            }
        }


        for (int j = 0; j < transposePic.height(); j++) {
            for (int i = 0; i < transposePic.width(); i++) {
                transposePic.set(i, j, colors[j * transposePic.width() + i]);
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
                cumulatedMatrix[i][j] = verticalHelper(cumulatedMatrix, j, i)
                        + cumulatedMatrix[i][j];
            }
        } //此处完成对能量积累矩阵的计算



        int[] path = new int[height()];
        int index = height() - 1;
        int x = 0;
        double min = cumulatedMatrix[height() - 1][0];
        for (int i = 1; i < width(); i++) {
            if (cumulatedMatrix[height() - 1][i] < min) {
                min = cumulatedMatrix[height() - 1][i];
                x = i;
            }
        }
        path[index] = x;
        index--;

        for (int i = cumulatedMatrix.length - 1; i >= 1; i--) { // 第一行不需要处理
            x = verticalHelper1(cumulatedMatrix, x, i);
            path[index] = x;
            index--;
        }


        return path;
    }

    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from picture
        if (seam.length != width() || !arrayLegal(seam)) {
            throw new java.lang.IllegalArgumentException("Passed in seam is not legal");
        }
        Picture seamedPic = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            int k = 0;
            for (int j = 0; j < height(); j++) {
                if (j != seam[i]) {
                    seamedPic.set(i, k, picture.get(i, j));
                    k++;
                }
            }
        }
        this.picture = seamedPic;
        setEnergyMatrix();
    }

    public void removeVerticalSeam(int[] seam) { // remove vertical seam from picture
        if (seam.length != height() || !arrayLegal(seam)) {
            throw new java.lang.IllegalArgumentException("Passed in seam is not legal");
        }
        Picture seamedPic = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            int k = 0;
            for (int i = 0; i < width(); i++) {
                if (i != seam[j]) {
                    seamedPic.set(k, j, picture.get(i, j));
                    k++;
                }
            }
        }
        this.picture = seamedPic;
        setEnergyMatrix();
    }

    private int xIndexRounding(int x) {
        //System.out.println(width() + (x % width()));
        if (x >= 0 && x <= width() - 1) {
            return x;
        } else if (x < 0) {
            return width() - 1;
        } else {
            return 0;
        }
    }

    private int yIndexRounding(int y) {
        if (y >= 0 && y <= height() - 1) {
            return y;
        } else if (y < 0) {
            return height() - 1;
        } else {
            return 0;
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
        if (width() >= 3) {
            if (x > 0 && x < width() - 1) {
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
        } else if (width() == 2) {
            return Math.min(matrix[y - 1][0], matrix[y - 1][1]);
        } else {
            return matrix[y - 1][0];
        }
    }

    private int verticalHelper1(double[][] matrix, int x, int y) {
        if (width() >= 3) {
            if (x > 0 && x < width() - 1) {
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
        } else if (width() == 2) {
            double t1 = matrix[y - 1][0];
            double t2 = matrix[y - 1][1];
            double min = Math.min(t1, t2);
            if (min == t1) {
                return 0;
            } else {
                return 1;
            }

        } else {
            return 0;
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
