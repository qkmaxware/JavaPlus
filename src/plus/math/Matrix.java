/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.math;

/**
 *
 * @author Colin Halseth
 */
public class Matrix {

    protected double[] mat;

    protected int height;
    protected int width;

    /**
     * Create a zero matrix of size X x Y
     * @param x
     * @param y
     */
    public Matrix(int x, int y) {
        this.width = x;
        this.height = y;

        this.mat = new double[this.width * this.height];
        
        for(int i = 0 ; i< mat.length; i++){
            mat[i] = 0;
        }
    }

    /**
     * Copy a matrix
     *
     * @param m
     */
    public Matrix(Matrix m) {
        this.height = m.height;
        this.width = m.width;

        this.mat = new double[this.width * this.height];
        for (int i = 0; i < m.mat.length; i++) {
            this.mat[i] = m.mat[i];
        }
    }

    /**
     * Create a matrix from a template
     *
     * @param matix
     */
    public Matrix(double[][] matix) {
        this.width = 0;
        this.height = matix.length;

        for (int i = 0; i < matix.length; i++) {
            if (matix[i].length > this.width) {
                this.width = matix[i].length;
            }
        }

        this.mat = new double[this.width * this.height];

        for(int i = 0 ; i< mat.length; i++){
            mat[i] = 0;
        }
        
        for (int i = 0; i < matix.length; i++) {
            for (int j = 0; j < matix[i].length; j++) {
                double f = matix[i][j];
                this.mat[GetIndex(i, j)] = f;
            }
        }
    }

    /**
     * Create a matrix from a flattened array
     * @param width
     * @param height
     * @param values 
     */
    public Matrix(int width, int height, double[] values){
        this.width = width;
        this.height = height;
        this.mat = new double[width*height];
        System.arraycopy(values, 0, mat, 0, Math.min(values.length, this.mat.length));
    }
    
    
    /**
     * Grab the 1D index for this 2D coordinate
     *
     * @param x
     * @param y
     * @return int
     */
    public int GetIndex(int x, int y) {
        return y * this.width + x;
    }
    
    /**
     * Get an element from the matrix in row, column order
     * @param x, the row
     * @param y, the column
     * @return 
     */
    public double Get(int x, int y){
        return this.mat[GetIndex(x,y)];
    }
    
    /**
     * Set an element in the matrix (only time this matrix is not immutable)
     * @param x
     * @param y
     * @param f 
     */
    public void Set(int x, int y, double f){
        this.mat[GetIndex(x,y)] = f;
    }
    
    /**
     * Get the width of this matrix
     *
     * @return int
     */
    public int GetWidth() {
        return this.width;
    }

    /**
     * Get the height of this matrix
     *
     * @return
     */
    public int GetHeight() {
        return this.height;
    }

    /**
     * Create a sub-matrix excluding one row and one column (-1 for no
     * exclusion)
     *
     * @param rowExclude
     * @param colExclude
     * @return matrix
     */
    public Matrix Submatrix(int rowExclude, int colExclude) {
        //exclude row -1, exclude col 1
        int w = GetWidth();
        int h = GetHeight();

        //Get submatrix size
        if (rowExclude >= 0 && rowExclude < h) {
            h -= 1;
        }
        if (colExclude >= 0 && colExclude < w) {
            w -= 1;
        }
        Matrix mat = new Matrix(w, h);

        int x = 0;
        for(int i = 0; i < this.GetWidth(); i++){
            if(i == colExclude){
                continue;
            }
            
            int y = 0;
            for(int j = 0; j < this.GetHeight(); j++){
                if(j == rowExclude){
                    continue;
                }
                
                mat.Set(x, y, this.Get(i, j));
                
                y++;
            }
            
            x++;
        }
        
        
        return mat;
    }

    /**
     * Creates the transposition of this matrix
     * @return matrix
     */
    public Matrix Transpose() {
        int oWidth = GetWidth();
        int oHeight = GetHeight();
        Matrix trans = new Matrix(oHeight, oWidth);
        for (int x = 0; x < oWidth; x++) {
            for (int y = 0; y < oHeight; y++) {
                trans.mat[GetIndex(y, x)] = this.mat[GetIndex(x, y)];
            }
        }
        return trans;
    }

    /**
     * Compute the determinant of this matrix
     * @return fraction
     */
    public double Determinant() {
            double det = 0;

            int w = GetWidth();
            int h = GetHeight();

            //Square Matries only
            if (w != h || w<=1 || h <=1) {
                throw new RuntimeException("Determinant can only be calculated for square matrices.");
            }
            //2x2 matrix --> recursive base case
            if (w == 2 && h == 2)
            {
                det = Get(0, 0)*( Get(1, 1) ) - ( Get(1, 0)*( Get(0, 1)));
            }
            else {
                //expand on first column aka id col 0
                int i = 0;
                for (int j = 0; j < h; j++) {
                    Matrix sub = Submatrix(j, i);
                    double subdet = sub.Determinant();
                    if (j % 2 != 0) { subdet = subdet*(-1); }
                    det = det + ( Get(i, j)*( subdet ));
                }
            }
            //Return answer
            return det;
        }
    
    /**
     * Compute the cofactor matrix of this matrix
     * @return matrix
     */
    public Matrix CofactorMatrix() {
            Matrix co = new Matrix(GetWidth(), GetHeight());

            for (int x = 0; x < GetWidth(); x++) {
                for (int y = 0; y < GetHeight(); y++)
                {
                    int multiplier = 1;
                    if (y % 2 != 0)
                    {
                        if (!(x % 2 != 0))
                        {
                            multiplier = -1;
                        }
                    }
                    else {
                        if ((x % 2 != 0))
                        {
                            multiplier = -1;
                        }
                    }
                    co.Set(x, y,Submatrix(y, x).Determinant()*(multiplier));
                }
            }

            return co;
        }
    
        /**
         * Compute the adjugate matrix of this matrix
         * @return matrix
         */
        public Matrix Adjugate() {
            return CofactorMatrix().Transpose();
        }
        
        /**
         * Compute the inverse of this matrix
         * @return matrix
         */
        public Matrix Invert() {
            int w = GetWidth();
            int h = GetHeight();

            Matrix invert = new Matrix(w, h);

            //Gota be a square matrix
            if (w != h || this.Determinant()==(0)) {
                throw new RuntimeException("Matrix is not invertable.");
            }

            if (w == 2 && h == 2) {
                //2x2 inversion is different
                Matrix m = new Matrix(2, 2);
                //Swap a&d, negate b,c
                m.Set(0, 0, this.Get(1, 1));
                m.Set(1, 1, this.Get(0, 0));
                m.Set(1, 0, this.Get(1, 0)*(-1));
                m.Set(0, 1, this.Get(0, 1)*(-1));
                return m.scale((1/(m.Determinant())));
            }

            if (w <= 1 && h <= 1) {
                //One by one inversion
                Matrix m = new Matrix(1,1);
                m.Set(0,0,1/this.Get(0, 0));
                return m;
            }

            return Adjugate().scale((1/(Determinant())));
        }
        
        /**
         * Print this matrix to a string
         * @return 
         */
        public String toString() {
            String str = "";
            for (int i = 0; i < this.GetWidth(); i++) {
                str += "[";

                for(int j = 0; j < this.GetHeight(); j++){
                    str += this.Get(i, j);
                    if(j != this.GetHeight() - 1){
                        str += " , ";
                    }
                }

                str += "]\n";
            }
            return str;
        }
        
        public String toLiteralString(){
            String s = "[";
            
            for(int i = 0; i < mat.length; i++){
                s+=mat[i]+", ";
            }
            
            return s+"]";
        }
        
        //----------------------------------------------------------------------
        //OPERATORS
        //----------------------------------------------------------------------
        
        /**
         * Multiply this matrix by another
         * @param b
         * @return 
         */
        public Matrix mul (Matrix b) //Multiply two m x n & n x a matrices if fails return empty 4x4
        {
            Matrix a = this;
            Matrix result = new Matrix(a.GetHeight(), b.GetWidth());
            if (a.GetHeight() == b.GetWidth())
            {
                for(int i = 0; i < a.GetWidth(); i++){
                    for(int j = 0; j < b.GetHeight(); j++){
                        double sum = 0;
                        for(int k = 0; k < a.GetHeight(); k++){
                            sum = sum+(a.Get(i, k)*(b.Get(k, j)));
                        }
                        result.Set(i, j, sum);
                    }
                }
            }
            else
            {
                throw new RuntimeException("Cannot multiply matrices, the number of rows in matrix a does not match the number of columns in matrix b.");
            }
            
            return result;
        }
        
        /**
         * Multiply this matrix by a doubleing point value
     * @param a
         * @return 
         */
        public Matrix scale(double a){
            Matrix b = this;
            Matrix mat = new Matrix(b.GetHeight(), b.GetWidth());
            for (int i = 0; i < b.GetHeight(); i++)
            {
                for (int j = 0; j < b.GetWidth(); j++)
                {
                    mat.Set(i, j, b.Get(i, j)*(a));
                }
            }
            return mat;
        }
        
        /**
         * Multiply this matrix by an integer value
     * @param a
         * @return 
         */
        public Matrix scale(int a){
            Matrix b = this;
            Matrix mat = new Matrix(b.GetHeight(), b.GetWidth());
            for (int i = 0; i < b.GetHeight(); i++)
            {
                for (int j = 0; j < b.GetWidth(); j++)
                {
                    mat.Set(i, j, b.Get(i, j)*(a));
                }
            }
            return mat;
        }
        
        /**
         * Add a matrix to this one
         * @param b
         * @return 
         */
        public Matrix add (Matrix b) //Add a vector to a matrix's last column
        {
            Matrix a = this;
            
            if ((a.GetHeight() != b.GetHeight()) && (a.GetWidth() != b.GetWidth())) {
                throw new RuntimeException("Addition of matrices is undefined for matrices of different sizes.");
            }

            Matrix m = new Matrix(a.GetHeight(), b.GetWidth());
            for (int x = 0; x < a.GetWidth(); x++) {
                for (int y = 0; y < a.GetHeight(); y++)
                {
                    m.Set(x, y, a.Get(x, y)+( b.Get(x, y)));
                }
            }

            return m;
        }
        
        /**
         * Subtract a matrix from this one
         * @param b
         * @return 
         */
        public Matrix sub (Matrix b) //Add a vector to a matrix's last column
        {
            Matrix a = this;
            
            if ((a.GetHeight() != b.GetHeight()) && (a.GetWidth() != b.GetWidth())) {
                throw new RuntimeException("Addition of matrices is undefined for matrices of different sizes.");
            }

            Matrix m = new Matrix(a.GetHeight(), b.GetWidth());
            for (int x = 0; x < a.GetWidth(); x++) {
                for (int y = 0; y < a.GetHeight(); y++)
                {
                    m.Set(x, y, a.Get(x, y)-( b.Get(x, y)));
                }
            }

            return m;
        }
}
