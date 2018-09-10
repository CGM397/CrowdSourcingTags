import numpy
import scipy
from math import sqrt
from sklearn.cluster import KMeans
from sys import argv

prefix = argv[1]

with open(prefix + '/param.txt', 'r') as fr:
    k = int(fr.read())
data = numpy.loadtxt(prefix + '/data.txt', delimiter=',')

estimator = KMeans(n_clusters=k)
estimator.fit(data)
labels = estimator.labels_
centroids = estimator.cluster_centers_

with open(prefix + '/frameResult.txt', 'w') as fw:
    for centroid in centroids:
        dist = numpy.empty([data.shape[0], 1], dtype=float)
        for i in range(data.shape[0]):
            dx = centroid[0] - data[i][0]
            dy = centroid[1] - data[i][1]
            dist[i] = sqrt(dx**2 + dy**2)

        size = numpy.size(dist)
        mean = numpy.mean(dist)
        std = numpy.std(dist)
        alpha = 1 - 0.95
        t_score = scipy.stats.t.isf(alpha / 2, df=(size-1))

        ME = t_score * std / numpy.sqrt(size)
        lower_limit = mean - ME
        upper_limit = mean + ME

        fw.write(str(centroid[0]).__add__(','))
        fw.write(str(centroid[1]).__add__(','))
        fw.write(str(lower_limit).__add__(','))
        fw.write(str(upper_limit))
        fw.write('\n')