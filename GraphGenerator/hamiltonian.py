from scipy import *
import numpy as np

# Le code de Feynman en langage Python :
# ecrit par sghiar le 7 mars 2016 a 09:28.
# ce code permet de trouver la matrice de Feynman et
# les cycles hamiltoniens s il existent .

# Debut du code

# On definit la fonction F de Feynman .

class Graph :
    def __init__(self,vertices_count,edges_count,links):
        self.vertices_count = vertices_count
        self.edges_count = edges_count
        self.links = links
        self.d = {}
        self.G = None

    @staticmethod
    def parseFile(filepath):
        g = []
        with open(filepath,'r') as f:
            while f.readable():
                vertices_count = int(f.readline().split(" : ")[1].rstrip())
                edges_count = int(f.readline().split(" : ")[1].rstrip())
                links = Graph.parselinks(f.readline().rstrip(),edges_count)
                g.append(Graph(vertices_count,edges_count,links))
                f.readline()
                if len(g) == 1000:
                    break
            return g

    @staticmethod
    def parselinks(line:str,edges_count):
        links = []
        elements = line.split("(")
        for i in range(0,edges_count):
            values = (elements[i+1][0:len(elements[i+1]) - 1]).split(',')
            links.append([int(values[0]),int(values[1])])
        return links
    @staticmethod
    def F(j, T):
        l = len(T)
        U = [l + 1]
        U = [0] * (l + 1)
        U[0] = T[0] - 1
        for i in range(1, l):
            U[i + 1] = T[i]
        U[1] = j
        return U

    def set_g(self):
        self.G =np.eye(self.vertices_count,self.vertices_count)
        for link in self.links:
            self.G[link[0]][link[1]] = 1

    def feyman_matrix_init(self) :
        d = self.d
        G = self.G
        d[0] = [[self.vertices_count,0]]
        n = self.vertices_count
        for j in range(n):
            if G[0][j] == 1 and 0 != j:
                d[j] = [[n - 1, j, 0]]
                # print d[j]
            else:
                d[j] = [[0, j]]
                # print d[j]
        for k in range(0, n ** 2):
            for j in range(n):
                if G[k % n][j] == 1 and (k % n) != j:
                    for T in d[k % n]:
                        if T[0] > 0:
                            d[j] += [Graph.F(j, T)]
                            # print d[j]
                if G[k % n][j] == 0:
                    d[j] = d[j]
                    # print d[j]

    def hamiltonian_cycle(self):
        L = []

        for h in self.d[0]:
            if len(h) == n + 2 and h[0] == 0 and h[1] == 0 and h[n + 1] == 0:
                if len(set(h)) == n:
                    del h[0]
                    # print h
                    L.append(h)

        H = []

        for elt in L:
            try:
                ind = H.index(elt)
            except:
                H.append(elt)

        if len(H) != 0:
            print(H)
        else:
            print(" Pas de cycles hamiltoniens ")

if __name__ == "__main__":
    graphs = Graph.parseFile("trace.txt")
    for graph in graphs:
        graph.set_g()
        graph.feyman_matrix_init()
        graph.hamiltonian_cycle()


# On construit le Grpahe G de particules :


# print (" Entrez un entier non nul > 1")
# n= int ( input ())
#
# G=np.eye(n,n)
#
#
# for i in range (n):
#     for j in range (n):
#         print(" Entrez G(i,j)", i, j)  # G(i,j)= 0 si i=j
#         G[i][j] = int(input())

# On construit la matrice de Feynman au point 0...

#
# d={}
#
# d[0]=[[n ,0]]

# for j in range (n):
#     if G [0][ j ]==1 and 0!= j :
#         d[j ]=[[n -1,j ,0]]
#         # print d[j]
#     else :
#         d[j]=[[0 ,j]]
#         # print d[j]
#
#
#
#
# for k in range (0,n **2) :
#     for j in range (n):
#         if G[k%n][j ]==1 and (k%n)!=j :
#             for T in d[k%n]:
#                 if T[0] > 0 :
#                     d[j ]+=[ F(j,T)]
#                     # print d[j]
#         if G[k%n][j]== 0 :
#             d[j]=d[j]
#             # print d[j]

# Remarque : n**2 est introduite car on a n tours a effectuer
# Matrice de Feynman au point 0

# print (" Matrice de Feynman au point 0 :")
#
# for T in d [0]:
#     if len (T)== n+2 and T [0]==0 and T [1]==0 and T[n+1]==0:
#         print (T)


# Cycles Hamiltoniens :

# print (" Cycles Hamiltoniens :")
#
# L =[]
#
# for h in d [0]:
#     if len (h)== n+2 and h [0]==0 and h [1]==0 and h[n+1]==0:
#         if len ( set (h))== n :
#             del h[0]
#             # print h
#             L.append (h)
#
# H =[]
#
# for elt in L:
#     try :
#         ind = H. index ( elt )
#     except :
#         H. append ( elt )
#
# if len (H) != 0:
#     print (H)
# else :
#     print (" Pas de cycles hamiltoniens ")

# Fin du code