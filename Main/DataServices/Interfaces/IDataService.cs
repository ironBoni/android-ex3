using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models {
    public interface IDataService<T, TKey> {
        List<T> GetAll();
        T GetById(TKey id);
        // Create, read, update, remove
        bool Create(T entity);
        bool Update(T entity);
        bool Delete(TKey id);
    }
}
