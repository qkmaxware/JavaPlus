/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.math;

import plus.system.Random;
import plus.system.functional.Func1;
import plus.system.functional.Func2;

/**
 *
 * @author Colin Halseth
 */
public class Matrix {

    private double[] values;
    private int rows;
    private int columns;

    /**
     * Create a row matrix from the values given
     * @param values
     * @return 
     */
    public static Matrix Row(double[] values) {
        Matrix m = new Matrix(1, values.length);
        m.values = values.clone();
        return m;
    }

    /**
     * Create a column matrix from the given values
     * @param values
     * @return 
     */
    public static Matrix Column(double[] values) {
        Matrix m = new Matrix(values.length, 1);
        m.values = values.clone();
        return m;
    }

    /**
     * Create an identity matrix with the given dimensions
     * @param rows
     * @param columns
     * @return 
     */
    public static Matrix Identity(int rows, int columns) {
        Matrix m = new Matrix(rows, columns);
        for (int i = 0; i < Math.min(rows, columns); i++) {
            m.Set(i, i, 1);
        }
        return m;
    }

    /**
     * Create a matrix of random values in the range {0..1}
     * @param rows
     * @param columns
     * @return 
     */
    public static Matrix Random(int rows, int columns){
        Matrix m = new Matrix(rows, columns);
        for (int i = 0; i < m.values.length; i++) {
            m.values[i] = Random.Next();
        }
        return m;
    }
    
    /**
     * Create a matrix with 
     * @param rows
     * @param columns 
     */
    public Matrix(int rows, int columns) {
        this.values = new double[columns * rows];
        this.columns = columns;
        this.rows = rows;
    }

    /**
     * Create a matrix with the given row and column values
     * @param values 
     */
    public Matrix(double[][] values) {
        int maxColumnSize = 0;
        int maxRowSize = values.length;

        //For each row, find largest #'s of columns
        for (int i = 0; i < values.length; i++) {
            if (values[i].length > maxColumnSize) {
                maxColumnSize = values[i].length;
            }
        }

        //Copy
        this.rows = maxRowSize;
        this.columns = maxColumnSize;
        this.values = new double[rows * columns];
        //For each row
        for (int i = 0; i < values.length; i++) {
            //For each column
            for (int j = 0; j < values[i].length; j++) {
                this.Set(i, j, values[i][j]);
            }
        }

    }

    /**
     * Copy another matrix
     * @param other 
     */
    public Matrix(Matrix other) {
        this.values = other.values.clone();
        this.rows = other.rows;
        this.columns = other.columns;
    }

    /**
     * Create a matrix from a flattened array
     * @param rows
     * @param columns
     * @param values 
     */
    public Matrix(int rows, int columns, double[] values) {
        this.rows = rows;
        this.columns = columns;
        this.values = new double[rows * columns];
        System.arraycopy(values, 0, this.values, 0, Math.min(values.length, this.values.length));
    }

    private int GetIndex2d(int row, int column) {
        return row * columns + column;
    }

    /**
     * Set a value in this matrix
     * @param row
     * @param column
     * @param v 
     */
    private void Set(int row, int column, double v) {
        values[GetIndex2d(row, column)] = v;
    }

    /**
     * Get a value from this matrix
     * @param row
     * @param column
     * @return 
     */
    public double Get(int row, int column) {
        return values[GetIndex2d(row, column)];
    }

    /**
     * Get the number of columns in the matrix
     * @return 
     */
    public int GetWidth() {
        return this.columns;
    }

    /**
     * Get the number of rows in the matrix
     * @return 
     */
    public int GetHeight() {
        return this.rows;
    }

    //Submatrix
    public Matrix SubMatrix(int rowExclude, int columnExclude){
        //exclude row -1, exclude col 1
        int w = GetWidth();
        int h = GetHeight();
        
        //Get submatrix size
        if (rowExclude >= 0 && rowExclude < h) {
            h -= 1;
        }
        if (columnExclude >= 0 && columnExclude < w) {
            w -= 1;
        }
        Matrix mat = new Matrix(h, w);
        
        int x = 0;
        for(int i = 0; i < this.GetWidth(); i++){
            if(i == columnExclude)
                continue;
            
            int y = 0;
            for(int j = 0; j < this.GetHeight(); j++){
                if(j == rowExclude)
                    continue;
                
                mat.Set(y, x, this.Get(j, i));
                y++;
            }
            
            x++;
        }
        
        return mat;
    }
    
    /**
     * Construct the transposition of this matrix
     * @return 
     */
    public Matrix Transpose() {
        Matrix m = new Matrix(this.columns, this.rows);
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                m.Set(c, r, this.Get(r, c));
            }
        }
        return m;
    }
    
    /**
     * Compute the determinant of this matrix
     * @return 
     */
    public double Determinant(){
        double det = 0;
        
        //Square matrices only
        int w = GetWidth();
        int h = GetHeight();
        if(w != h || w <=1 || h <= 1){
            throw new RuntimeException("Determinant can only be calculated for square matrices only.");
        }
        
        //2x2 recursion base
        if(w == 2 && h == 2){
            det = Get(0,0)*Get(1,1) - Get(0,1)*Get(1,0);
        }
        //Larger matrices expand along first column
        else{
            int i = 0; //column 0
            for(int j = 0; j < h; j++){ //Row j
                Matrix sub = SubMatrix(j,i);
                double subdet = sub.Determinant();
                if(j % 2 != 0){subdet = subdet * -1;}
                det = det + Get(j,i) * subdet;
            }
        }
        
        return det;
    }

    /**
     * Compute the cofactor matrix of this matrix
     * @return 
     */
    public Matrix CofactorMatrix(){
        Matrix co = new Matrix(GetWidth(), GetHeight());

            for (int x = 0; x < GetWidth(); x++) { //column
                for (int y = 0; y < GetHeight(); y++) { //row
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
                    co.Set(y, x,SubMatrix(y, x).Determinant()*(multiplier));
                }
            }

            return co;
    }
    
    /**
     * Compute the adjugate matrix of this matrix
     * @return 
     */
    public Matrix Adjugate(){
        return this.CofactorMatrix().Transpose();
    }
    
    /**
     * Compute the inverse of this matrix
     * @return 
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
    
    //Invert
    
    //--------------------------------------------------------------------------
    //Operators
    //--------------------------------------------------------------------------
    
    /**
     * Horizontally concatenate the values in 2 matrices
     * @param a
     * @param b
     * @return 
     */
    public static Matrix Concat(Matrix a, Matrix b){
        Matrix m = new Matrix(Math.max(a.rows, b.rows), a.columns + b.columns);
        for(int i = 0; i < a.rows; i++){
            for(int j = 0; j < a.columns; j++){
                m.Set(i, j, a.Get(i, j));
            }
        }
        
        for(int i = 0; i < b.rows; i++){
            for(int j = 0; j < b.columns; j++){
                m.Set(i,(a.columns) + j, b.Get(i, j));
            }
        }
        return m;
    }
    
    /**
     * Perform some kind of operation on each element of this matrix
     * @param fn
     * @return new matrix where each element is determined from fn(this)
     */
    public Matrix operate(Func1<Double, Double> fn) {
        Matrix m = new Matrix(this);
        for (int i = 0; i < m.values.length; i++) {
            m.values[i] = fn.Invoke(m.values[i]);
        }
        return m;
    }
    
    /**
     * Perform a component wise operation on same size matrices
     * @param a
     * @param b
     * @param fn
     * @return 
     */
    public static Matrix operate(Matrix a, Matrix b, Func2<Double,Double,Double> fn){
        return a.operate(b, fn);
    }
    
    /**
     * Perform a component wise operation on same size matrices
     * @param other
     * @param fn
     * @return 
     */
    public Matrix operate(Matrix other, Func2<Double,Double,Double> fn){
        if(other.GetWidth() != this.GetWidth() || other.GetHeight() != this.GetHeight())
            throw new RuntimeException("Cannot perform component wise operations if matrices have differing dimensions.");
        
        Matrix m = new Matrix(this);
        for(int i = 0; i < m.values.length; i++){
            m.values[i] = fn.Invoke(this.values[i], other.values[i]);
        }
        return m;
    }

    /**
     * Scale all the values in this matrix by a scalar value
     * @param s
     * @return s * this
     */
    public Matrix scale(double s) {
        Matrix m = new Matrix(this);
        for (int i = 0; i < m.values.length; i++) {
            m.values[i] = m.values[i] * s;
        }
        return m;
    }

    /**
     * Add two matrices together
     * @param a
     * @param b
     * @return 
     */
    public static Matrix add(Matrix a, Matrix b){
        return a.add(b);
    }
    
    /**
     * Add two matrices together
     * @param m
     * @return this + other
     */
    public Matrix add(Matrix m) {
        if (m.GetWidth() != this.GetWidth() || m.GetHeight() != this.GetHeight()) {
            throw new RuntimeException("Cannot add matrixes of different sizes. Check the dimentions before adding.");
        }

        Matrix r = new Matrix(m.rows, m.columns);
        for (int i = 0; i < this.values.length; i++) {
            r.values[i] = this.values[i] + m.values[i];
        }
        return r;
    }

    /**
     * Subtract two matrices from each other
     * @param a
     * @param b
     * @return 
     */
    public static Matrix sub(Matrix a, Matrix b){
        return a.sub(b);
    }
    
    /**
     * Subtract two matrices from each other
     * @param m
     * @return this - other
     */
    public Matrix sub(Matrix m) {
        if (m.GetWidth() != this.GetWidth() || m.GetHeight() != this.GetHeight()) {
            throw new RuntimeException("Cannot subtract matrixes of different sizes. Check the dimentions before subtracting.");
        }

        Matrix r = new Matrix(m.rows, m.columns);
        for (int i = 0; i < this.values.length; i++) {
            r.values[i] = this.values[i] - m.values[i];
        }
        return r;
    }

    /**
     * Post-multiply matrix 'b' to matrix 'a'
     * @param a
     * @param b
     * @return 
     */
    public static Matrix mul(Matrix a, Matrix b){
        return a.mul(b);
    }
    
    /**
     * Post-multiply another matrix to this matrix
     * @param other
     * @return this * other
     */
    public Matrix mul(Matrix other) {
        //if this is an n x m matrix and other is an m x p matrix then
        //result will be an n x p matrix

        if (this.GetWidth() != other.GetHeight()) {
            throw new RuntimeException("Cannot multiply matrices. Dimentions do not match.");
        }

        Matrix r = new Matrix(this.GetHeight(), other.GetWidth());

        for (int i = 0; i < this.GetHeight(); i++) {
            for (int j = 0; j < other.GetWidth(); j++) {
                double sum = 0;
                for (int k = 0; k < this.GetWidth(); k++) {
                    sum += this.Get(i, k) * other.Get(k, j);
                }
                r.Set(i, j, sum);
            }
        }

        return r;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int row = 0; row < this.rows; row++) {
            builder.append("{");
            for (int column = 0; column < this.columns; column++) {
                builder.append(this.Get(row, column));
                builder.append((column != this.columns - 1)? "," : "");
            }
            builder.append("}");
            builder.append((row != this.rows - 1)? "," : "");
        }
        return builder.append("}").toString();
    }
    
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (int row = 0; row < this.rows; row++) {
            builder.append("\t{");
            for (int column = 0; column < this.columns; column++) {
                builder.append(this.Get(row, column));
                builder.append((column != this.columns - 1)? "," : "");
            }
            builder.append("}");
            builder.append((row != this.rows - 1)? ",\n" : "\n");
        }
        return builder.append("}").toString();
    }
}
