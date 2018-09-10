import jieba
import math
import numpy as np
import os
from sys import argv

prefix = argv[1]

def read_from_file(file_name):
    with open(file_name, "r", encoding='utf-8') as fp:
        words = fp.read()
    return words

def stop_words(stop_word_file):
    words = read_from_file(stop_word_file)
    result = jieba.cut(words)
    new_words = []
    for r in result:
        new_words.append(r)
    return set(new_words)


def gen_sim(A, B):
    num = float(np.dot(A, B.T))
    denum = np.linalg.norm(A) * np.linalg.norm(B)
    if denum == 0:
        denum = 1
    cosn = num / denum
    sim = 0.5 + 0.5 * cosn
    return sim


def del_stop_words(words, stop_words_set):
    result = jieba.cut(words)
    new_words = []
    for r in result:
        if r not in stop_words_set:
            new_words.append(r)
    return new_words


def tfidf(term, doc, word_dict, docset):
    tf = float(doc.count(term)) / (len(doc) + 0.001)
    idf = math.log(float(len(docset)) / word_dict[term])
    return tf * idf


def idf(term, word_dict, docset):
    idf = math.log(float(len(docset)) / word_dict[term])
    return idf


def word_in_docs(word_set, docs):
    word_dict = {}
    for word in word_set:
        word_dict[word] = len([doc for doc in docs if word in doc])
    return word_dict


def get_all_vector(file_path, stop_words_set):
    names = [os.path.join(file_path, f) for f in os.listdir(file_path)]
    posts = [open(name, encoding='utf-8').read() for name in names]
    docs = []
    word_set = set()
    for post in posts:
        doc = del_stop_words(post, stop_words_set)
        docs.append(doc)
        word_set |= set(doc)

    word_set = list(word_set)
    docs_vsm = []
    for doc in docs:
        temp_vector = []
        for word in word_set:
            temp_vector.append(doc.count(word) * 1.0)
        docs_vsm.append(temp_vector)

    docs_matrix = np.array(docs_vsm)
    column_sum = [float(len(np.nonzero(docs_matrix[:, i])[0]))
                  for i in range(docs_matrix.shape[1])]
    column_sum = np.array(column_sum)
    column_sum = docs_matrix.shape[0] / column_sum
    idf = np.log(column_sum)
    idf = np.diag(idf)
    for doc_v in docs_matrix:
        if doc_v.sum() == 0:
            doc_v = doc_v / 1
        else:
            doc_v = doc_v / (doc_v.sum())

    tfidf = np.dot(docs_matrix, idf)
    return names, tfidf


if __name__ == "__main__":
    stop_words = stop_words(prefix + "/stopwords.txt")
    names, tfidf_mat = get_all_vector(prefix + "/sentences/", stop_words)
    tfidf_mat = np.asarray(tfidf_mat)
    n = names.__len__()
    #print(names)
    #print(tfidf_mat)
    with open(prefix + '/param.txt', 'r') as fr:
        k = int(fr.readline())

    k = min(k, n - 1)
    rating = []
    for i in range(n - 1):
        #print(tfidf_mat[i + 1][0])
        rating.append((tfidf_mat[i + 1][0], i + 1))
    rating = sorted(rating, key=(lambda x: x[0]), reverse=True)

    with open(prefix + '/result.txt', 'w') as fw:
        for i in range(k):
            fw.write(str(rating[i][1]) + '\n')

