/*package de.oganisyan.paraglidervario.util;


public class Pair<X,Y> {

   private X f;
   private Y s;


   public Pair(X var1, Y var2) {
      this.f = var1;
      this.s = var2;
   }

   public boolean equals(Pair<X, Y> var1) {
      boolean var2 = var1 instanceof Pair;
      boolean var3 = false;
      if(var2) {
         Pair<X,Y> var4 = (Pair<X, Y>)var1;
         if(this.f != var4.f) {
            X var8 = this.f;
            var3 = false;
            if(var8 == null) {
               return var3;
            }

            X var9 = var4.f;
            var3 = false;
            if(var9 == null) {
               return var3;
            }

            boolean var10 = this.f.equals(var4.f);
            var3 = false;
            if(!var10) {
               return var3;
            }
         }

         if(this.s != var4.s) {
            Y var5 = this.s;
            var3 = false;
            if(var5 == null) {
               return var3;
            }

            Object var6 = var4.s;
            var3 = false;
            if(var6 == null) {
               return var3;
            }

            boolean var7 = this.s.equals(var4.s);
            var3 = false;
            if(!var7) {
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   public Object getFirst() {
      return this.f;
   }

   public Object getSecond() {
      return this.s;
   }

   public int hashCode() {
      int var1;
      if(this.f != null) {
         var1 = this.f.hashCode();
      } else {
         var1 = 0;
      }

      int var2;
      if(this.s != null) {
         var2 = this.s.hashCode();
      } else {
         var2 = 0;
      }

      return var1 + var2 * (var1 + var2);
   }

   public void setFirst(X var1) {
      this.f = var1;
   }

   public void setSecond(Y var1) {
      this.s = var1;
   }

   public String toString() {
      return "(" + this.f + ", " + this.s + ")";
   }
}
*/

package de.oganisyan.paraglidervario.util;

public class Pair {
	private Object f;
	private Object s;

	public Pair(Object arg1, Object arg2) {
		super();
		this.f = arg1;
		this.s = arg2;
	}

	public boolean equals(Object other) {
		boolean v1 = false;
		if((other instanceof Pair)) {
			Object v0 = other;
			if(this.f != ((Pair)v0).f) {
				if(this.f == null) {
					return v1;
				}
				else if(((Pair)v0).f != null) {
					if(this.f.equals(((Pair)v0).f)) {
						if(this.s != ((Pair)v0).s) {
							if(this.s == null) {
								v1 = true;
							}
							else if(((Pair)v0).s != null) {
								if(this.s.equals(((Pair)v0).s)) {
									v1 = true;
								}

								v1 = true;
							}
							else {
								v1 = true;
							}
						}
						v1 = true;
						return v1;
					}

					v1 = true;
				}
				else {
					v1 = true;
				}
			}

			if(this.s != ((Pair)v0).s) {
				if(this.s == null) {
					return v1;
				}
				else if(((Pair)v0).s != null) {
					if(this.s.equals(((Pair)v0).s)) {
						v1 = true;
					}

					return v1;
				}
				else {
					return v1;
				}
			}

			v1 = true;

		}

		return v1;
	}

	public Object getFirst() {
		return this.f;
	}

	public Object getSecond() {
		return this.s;
	}

	public int hashCode() {
		int v1;
		int v0;
		if(this.f != null) {
			v0 = this.f.hashCode();
		}
		else {
			v0 = 0;
		}

		if(this.s != null) {
			v1 = this.s.hashCode();
		}
		else {
			v1 = 0;
		}

		return (v0 + v1) * v1 + v0;
	}

/*	public void setFirst(Object arg1) {
		this.f = arg1;
	}

	public void setSecond(Object arg1) {
		this.s = arg1;
	}*/

	public String toString() {
		return "(" + this.f + ", " + this.s + ")";
	}
}