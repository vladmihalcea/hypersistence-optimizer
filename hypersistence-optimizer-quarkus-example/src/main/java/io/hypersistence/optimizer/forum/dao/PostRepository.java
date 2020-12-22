/*
 * Copyright (c) 2019, Mihalcea Vlad-Alexandru (https://vladmihalcea.com)
 * All rights reserved.
 *
 * Mihalcea Vlad-Alexandru grants the Customer the non-exclusive,
 * timely limited and non-transferable license to install and use the Software
 * under the terms of the Hypersistence Optimizer License Agreement.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * See the Hypersistence Optimizer License Agreement for more details:
 *
 * https://vladmihalcea.com/hypersistence-optimizer/license
 */
package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, Long> {

    public List<Post> findByTitle(String title) {
        return find("FROM Post p WHERE p.title = ?1", title).list();
    }
}
