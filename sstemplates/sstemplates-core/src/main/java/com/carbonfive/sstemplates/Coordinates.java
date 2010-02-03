package com.carbonfive.sstemplates;

public class Coordinates
    implements Comparable
{
  private int row;
  private short column;

  public int getRow()
  {
    return row;
  }

  public short getColumn()
  {
    return column;
  }

  public Coordinates(int row, short column)
  {
    this.row = row;
    this.column = column;
  }

  public boolean equals(Object o)
  {
    if (o == null) return false;
    Coordinates p = (Coordinates)o;
    return ((row == p.getRow()) &&
        (column == p.getColumn()));
  }

  public int hashCode()
  {
    return row ^ column;
  }

  public String toString()
  {
    return columnString(column) + (row + 1);
  }

  private String columnString(short column)
  {
    if (column >=0 && column < 26)
      return "" + (char) ( column + 65 );

    if (column >= 26)
    {
      int first = (column / 26) - 1;
      int second = column % 26;
      return "" + ((char) (first + 65)) + ((char) (second + 65));
    }

    throw new IllegalStateException("Bad column number: " + column);
  }

  public int compareTo(Object o)
  {
    Coordinates c = (Coordinates)o;
    if (getRow() < c.getRow()) return -1;
    if (getRow() > c.getRow()) return 1;
    if (getColumn() < c.getColumn()) return -1;
    if (getColumn() > c.getColumn()) return 1;
    return 0;
  }
}
