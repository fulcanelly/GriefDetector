package me.fulcanelly.grdetector.warn.data;

public enum WorldType {
  REGUALAR {
    @Override
    public String toString() {
      return "-звичайний-";
    }
  },
  NETHER {
    @Override
    public String toString() {
      return "-пекло-";
    }
  }, 
  THE_END {
    @Override
    public String toString() {
      return "-край-";
    }
  },
  UNKNOWN,  
}
